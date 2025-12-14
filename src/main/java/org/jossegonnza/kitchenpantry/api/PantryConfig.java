package org.jossegonnza.kitchenpantry.api;

import org.jossegonnza.kitchenpantry.domain.Pantry;
import org.jossegonnza.kitchenpantry.infrastructure.persistence.jdbc.JdbcPantryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PantryConfig {
    @Bean
    public Pantry pantry(JdbcPantryRepository repository) {
        return new Pantry(repository);
    }
}
