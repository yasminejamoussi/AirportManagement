package com.example.Airport_Management.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.Airport_Management.passager.TwilioService;

@Configuration
public class TwilioConfig {
    
    @Bean
    @ConditionalOnMissingClass("com.twilio.Twilio")
    public TwilioService twilioService() {
        return null;
    }
} 