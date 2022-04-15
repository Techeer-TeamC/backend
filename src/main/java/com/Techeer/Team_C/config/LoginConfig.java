package com.Techeer.Team_C.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoginConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}