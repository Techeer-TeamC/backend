package com.Techeer.Team_C;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class TeamCApplication {

	public static void main(String[] args) {
		SpringApplication.run(TeamCApplication.class, args);
	}

}
