package org.jossegonnza.pantry;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PantryTest {
    @Test
    void shouldAddNewProductToEmptyPantry() {
        Pantry pantry = new Pantry();

        pantry.add("Rice");
        List<Product> products = pantry.getProducts();

        assertEquals(1, products.size());
        assertEquals("Rice", products.get(0).getName());
    }
}