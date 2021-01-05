package io.github.kprasad99.streams;

import java.time.Duration;
import java.util.function.BiFunction;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.StreamJoined;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.kprasad99.streams.proto.Department;
import io.github.kprasad99.streams.proto.DepartmentData;
import io.github.kprasad99.streams.proto.Employee;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class StreamsConfiguration {

	@Bean
	public BiFunction<KStream<String, Employee>, KStream<String, Department>, KStream<String, DepartmentData>> process() {
		return (emp, dept) -> {
			var s0 = emp.selectKey((k, v) -> v.getDeptId()).through("kp.internal.employee",
					Produced.with(Serdes.String(), AppSerdes.employee()));
			return s0.leftJoin(dept, (v1, v2) -> {
				if (v2 == null) {
					log.info("No Department is present");
					return null;
				} else {
					var data = DepartmentData.newBuilder();
					data.setId(v2.getId());
					data.setName(v2.getName());
					data.addEmployees(v1);
					return data.build();
				}
			}, JoinWindows.of(Duration.ofMinutes(1)), StreamJoined.with(Serdes.String(), AppSerdes.employee(),AppSerdes.department())).peek((k, v) -> {
				log.info("Key->{}, value->{}", k, v);
			}).filter((k, v) -> v != null);
		};

	}
}