package com.example.employees.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI employeesLongestPeriodOfWorkTogether() {
        return new OpenAPI()
                .info(new Info().title("Employees longest period of work together")
                        .description("Application for finding out which two employees have been working together for " +
                                "the longest period of time.")
                        .version("1.0"));
    }
}
