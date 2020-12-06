package io.github.kprasad99.streams.ep;

import io.github.kprasad99.streams.proto.Department;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.github.kprasad99.streams.proto.Employee;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class AppEndpoint {

	private final KafkaTemplate<String, Employee> empKafka;
	private final KafkaTemplate<String, Department> depKafka;
	private final ModelMapper modelMapper;

	@PostMapping("/employee")
	public Mono<Long> save(@RequestBody io.github.kprasad99.streams.ep.domain.Employee emp){
		var data = toProto(emp);
		var result = empKafka.send("kp.employee", emp.getId(), data.build());
		return Mono.fromFuture(result.completable()).map(e->e.getRecordMetadata().offset());
	}

	@PostMapping("/department")
	public Mono<Long> save(@RequestBody io.github.kprasad99.streams.ep.domain.Department dept){
		var data = toProto(dept);
		var result = depKafka.send("kp.department", dept.getId(),data.build());
		return Mono.fromFuture(result.completable()).map(e->e.getRecordMetadata().offset());
	}

	private Employee.Builder toProto(io.github.kprasad99.streams.ep.domain.Employee emp) {
		return modelMapper.map(emp, Employee.Builder.class);
	}

	private Department.Builder toProto(io.github.kprasad99.streams.ep.domain.Department dept) {
		return modelMapper.map(dept, Department.Builder.class);
	}

}
