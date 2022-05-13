package com.Techeer.Team_C.global.config;

import com.Techeer.Team_C.domain.user.repository.UserMemoryRepository;
import com.Techeer.Team_C.domain.user.repository.UserRepository;
import com.Techeer.Team_C.domain.user.service.UserService;
import org.json.simple.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.spring.web.json.Json;

@Configuration
public class LoginConfig {

    private final UserRepository userRepository;

    @Autowired
    public LoginConfig(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public UserService userService() {
        return new UserService(userRepository,modelMapper());
    }

//    @Bean
//    public UserRepository userRepository(){
//        //return new UserMemoryRepository;
//    }    //memoryDb용


}
