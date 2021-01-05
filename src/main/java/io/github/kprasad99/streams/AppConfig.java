package io.github.kprasad99.streams;

import org.apache.kafka.common.serialization.Serde;
import org.modelmapper.ModelMapper;
import org.modelmapper.protobuf.ProtobufModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.kprasad99.streams.proto.Department;
import io.github.kprasad99.streams.proto.DepartmentData;
import io.github.kprasad99.streams.proto.Employee;
import io.github.kprasad99.streams.serde.ProtobufSerde;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper(){
    	var mapper = new ModelMapper();
    	mapper.registerModule(new ProtobufModule());
        return mapper;
    }
    
    @Bean
    public Serde<Employee> employeeSerde(){
    	return new ProtobufSerde<>(Employee.class);
    }
    
    @Bean
    public Serde<Department> departSerde(){
    	return new ProtobufSerde<>(Department.class);
    }
    
    @Bean
    public Serde<DepartmentData> departDataSerde(){
    	return new ProtobufSerde<>(DepartmentData.class);
    }

}
