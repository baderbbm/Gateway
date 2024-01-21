package com.microservices.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;


@Configuration
public class GatewayConfig {
	
	// Définir des routes personnalisées pour diriger le trafic vers différents microservices
	
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("patients_all_route", r -> r
                        .path("/patients/all")
                        .uri("http://localhost:8080")) // l'URL du microservice backend

                .route("patients_id_route", r -> r
                        .path("/patients/{patientId}")
                        .uri("http://localhost:8080")) // l'URL du microservice backend
                .build();
    }
}
