package org.jossegonnza.kitchenpantry.api;

import org.jossegonnza.kitchenpantry.domain.Pantry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PantryConfig {
    @Bean
    public Pantry pantry() {
        return new Pantry();
    }
}
