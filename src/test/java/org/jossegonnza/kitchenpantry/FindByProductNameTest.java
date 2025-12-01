package org.jossegonnza.kitchenpantry;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FindByProductNameTest {
    @Test
    void shouldFindProductByNameWhenItExists() {
        Pantry pantry = new Pantry();
        pantry.addProduct("Rice");
        pantry.addProduct("Pasta");

        Optional<Product> product = pantry.findByName("Rice");

        assertTrue(product.isPresent());
        assertEquals("Rice", product.get().getName());
    }

    @Test
    void shouldReturnEmptyWhenProductDoesNotExist() {
        Pantry pantry = new Pantry();
        pantry.addProduct("Rice");

        Optional<Product> product = pantry.findByName("Pasta");

        assertTrue(product.isEmpty());
    }
}
