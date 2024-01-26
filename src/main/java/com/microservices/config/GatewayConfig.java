package com.microservices.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;


@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("patients_all_route", r -> r
                        .path("/patients/**")
                        .uri("http://localhost:8082"))

                .route("update_adresse_route", r -> r
                        .method("PUT")
                        .and()
                        .path("/patients/**")
                        .uri("http://172.17.0.2:8082"))
           
                .route("add_patient_route", r -> r
                        .method("POST")
                        .and()
                        .path("/patients/**")
                        .uri("http://172.17.0.2:8082"))
                .build();
    }
}
