package com.Techeer.Team_C.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "https://fish-it-c.herokuapp.com/", "http://fish-it-c.herokuapp.com/")
                .allowedMethods("*"); //get,post,patch 등 모든 허용할 HTTP method정의
    }
}
