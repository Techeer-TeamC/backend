package com.Techeer.Team_C.config;

import com.Techeer.Team_C.repository.UserMemoryRepository;
import com.Techeer.Team_C.repository.UserRepository;
import com.Techeer.Team_C.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoginConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public UserService userService() {
        return new UserService(userRepository(),modelMapper());
    }

    @Bean
    public UserRepository userRepository(){
        return new UserMemoryRepository();
    }
    //향후 mysql과 관련된 모델을 구현후 변경할 예정 (return new UsermysqlRepository();

}