package com.shounoop.carrentalspring.configuration;

import com.stripe.Stripe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig {

    @Value("${stripe.api.key}")
    private String apiKey;

    @Bean
    public Stripe stripe() {
        Stripe.apiKey = apiKey;
        return null; // Return null as the bean itself isn't used directly
    }
}