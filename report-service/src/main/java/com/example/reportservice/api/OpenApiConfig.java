package com.example.reportservice.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Report Service API",
                version = "v1",
                description = "API for generating and downloading reports"
        )
)
public class OpenApiConfig {
}