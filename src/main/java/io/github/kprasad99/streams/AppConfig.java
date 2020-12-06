package io.github.kprasad99.streams;

import org.modelmapper.ModelMapper;
import org.modelmapper.protobuf.ProtobufModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper(){
    	var mapper = new ModelMapper();
    	mapper.registerModule(new ProtobufModule());
        return mapper;
    }
}
