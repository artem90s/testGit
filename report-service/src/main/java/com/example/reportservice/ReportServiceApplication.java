package com.example.reportservice;

import com.example.reportservice.config.ReportProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ReportProperties.class)
public class ReportServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReportServiceApplication.class, args);
    }
}