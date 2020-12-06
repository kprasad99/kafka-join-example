package io.github.kprasad99.streams;

import io.github.kprasad99.streams.proto.Department;
import io.github.kprasad99.streams.proto.Employee;
import io.github.kprasad99.streams.protobuf.serialization.ProtobufSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class ProducerConfiguration {

	@Bean
	public ProducerFactory<String, Employee> employeeProducerFactory(KafkaProperties props){
		var config = props.buildAdminProperties();
		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ProtobufSerializer.class);
		return new DefaultKafkaProducerFactory<>(config);
	}

	@Bean
	public ProducerFactory<String, Department> departProducerFactory(KafkaProperties props){
		var config = props.buildAdminProperties();
		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ProtobufSerializer.class);
		return new DefaultKafkaProducerFactory<>(config);
	}

	@Bean
	public KafkaTemplate<String, Employee> employeeKafkaTemplate(ProducerFactory<String, Employee> factory){
		return new KafkaTemplate<>(factory);
	}

	@Bean
	public KafkaTemplate<String, Department> departmentKafkaTemplate(ProducerFactory<String, Department> factory){
		return new KafkaTemplate<>(factory);
	}

}
