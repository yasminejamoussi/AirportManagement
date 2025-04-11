package com.example.Airport_Management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfiguration;

@SpringBootApplication
@EnableDiscoveryClient // Si vous utilisez un service discovery comme Eureka
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

    /*@Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("Passager", r -> r.path("/api/passager/**")
                        .uri("http://localhost:8091"))
                .route("LivraisonBagages", r -> r.path("/api/livraisonbagage/**")
                        .uri("http://localhost:8090"))
                .route("Forum", r -> r.path("/forum/**")
                        .uri("http://localhost:8085"))
                .route("Reclamation", r -> r.path("/reclamation/**")
                        .uri("http://localhost:8082"))
                .route("ObjetPerdu", r -> r.path("/objetperdu/**")
                        .uri("http://localhost:8081"))
                .build();
    }*/
}