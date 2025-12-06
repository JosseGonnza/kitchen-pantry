package org.jossegonnza.kitchenpantry.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductQuantityTest {
    @Test
    void shouldNewProductStartWithZeroQuantity() {
        Pantry pantry = new Pantry();
        pantry.addProduct("Rice", Category.GRAINS);

        Product rice = pantry.findByName("Rice")
                .orElseThrow(() -> new AssertionError("Product 'Rice' should exist"));

        assertEquals(0, rice.getQuantity());
    }
}
