package com.example.Airport_Management.reclamation;

import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

@Configuration
public class FeignConfig {
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL; // Pour voir ce qui se passe
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication != null && authentication.getCredentials() instanceof Jwt) {
                    String token = ((Jwt) authentication.getCredentials()).getTokenValue();
                    template.header("Authorization", "Bearer " + token);
                    System.out.println("Token envoyé à Passager : Bearer " + token); // Pour déboguer
                } else {
                    System.out.println("Aucun token valide trouvé dans le contexte de sécurité");
                }
            }
        };
    }
}