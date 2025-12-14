package org.jossegonnza.kitchenpantry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.jossegonnza.kitchenpantry")
public class KitchenPantryApplication {

    public static void main(String[] args) {
        SpringApplication.run(KitchenPantryApplication.class, args);
    }
}