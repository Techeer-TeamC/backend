package com.Techeer.Team_C.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@Component
public class SwaggerConfig {

    @Bean
    public Docket api2() {
        return new Docket(DocumentationType.OAS_30)
            .useDefaultResponseMessages(false)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.Techeer.Team_C.controller"))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(new ApiInfoBuilder().title("Techeer Team C Swagger")
                .description("specification")
                .version("1.0")
                .build());
    }
}
