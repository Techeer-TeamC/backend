package com.Techeer.Team_C.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

// repository의 자동 bean 생성 및 주입을 위함.
@Configuration
@EnableJpaRepositories(basePackages = "com.Techeer.Team_C.repository")
public class AppConfig {
}
