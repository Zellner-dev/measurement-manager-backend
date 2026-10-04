package com.zellner.workoutmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI workoutManagerOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Workout Manager API")
                        .description("REST API for managing workouts, exercises, workout logs and body measurements")
                        .version("0.0.1"));
    }

}
