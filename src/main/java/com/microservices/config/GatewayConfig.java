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
        		// Routes pour le microservice Patient
                .route("patients_all_route", r -> r
                        .path("/patients/**")
                      //  .uri("http://192.168.1.4:8082"))
                       .uri("http://localhost:8082"))

                .route("update_adresse_route", r -> r
                        .method("PUT")
                        .and()
                        .path("/patients/**")
                     //   .uri("http://192.168.1.4:8082"))
                        .uri("http://localhost:8082"))

           
                .route("add_patient_route", r -> r
                        .method("POST")
                        .and()
                        .path("/patients/**")
                       // .uri("http://192.168.1.4:8082"))
                       .uri("http://localhost:8082"))
                
                // Route pour le microservice Medecin
                .route("medecin_notes_route", r -> r
                        .path("/medecin/**")
                       // .uri("http://192.168.1.5:8083"))
                        .uri("http://localhost:8083"))
                
             // Route pour le microservice de calcul du risque de diabète
                .route("diabetes_risk_route", r -> r
                        .path("/diabetes-risk/**")
                      //  .uri("http://192.168.1.6:8084")) 
                        .uri("http://localhost:8084")) 
                
                .build();
    }
}
