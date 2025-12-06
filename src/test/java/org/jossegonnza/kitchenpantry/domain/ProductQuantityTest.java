package org.jossegonnza.kitchenpantry.domain;

import org.jossegonnza.kitchenpantry.domain.exception.ProductNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProductQuantityTest {
    @Test
    void shouldNewProductStartWithZeroQuantity() {
        Pantry pantry = new Pantry();
        pantry.addProduct("Rice", Category.GRAINS);

        Product rice = pantry.findByName("Rice")
                .orElseThrow(() -> new AssertionError("Product 'Rice' should exist"));

        assertEquals(0, rice.getQuantity());
    }

    @Test
    void shouldIncreaseProductQuantity() {
        Pantry pantry = new Pantry();
        pantry.addProduct("Rice", Category.GRAINS);

        pantry.increaseQuantity("Rice", 5);

        Product rice = pantry.findByName("Rice")
                .orElseThrow(() -> new AssertionError("Product 'Rice' should exist"));

        assertEquals(5, rice.getQuantity());
    }

    @Test
    void shouldNotAllowNonPositiveIncrease() {
        Pantry pantry = new Pantry();
        pantry.addProduct("Rice", Category.GRAINS);

        assertThrows(IllegalArgumentException.class,
                () -> pantry.increaseQuantity("Rice", 0));
        assertThrows(IllegalArgumentException.class,
                () -> pantry.increaseQuantity("Rice", -3));
    }

    @Test
    void shouldThrowWhenIncreasingQuantityOfNonExistingProduct() {
        Pantry pantry = new Pantry();

        assertThrows(ProductNotFoundException.class,
                () -> pantry.increaseQuantity("Rice", 3));
    }

    @Test
    void shouldDecreaseProductQuantity() {
        Pantry pantry = new Pantry();
        pantry.addProduct("Rice", Category.GRAINS);

        pantry.increaseQuantity("Rice", 10);
        pantry.decreaseQuantity("Rice", 5);
        Product rice = pantry.findByName("Rice")
                .orElseThrow(() -> new AssertionError("Product 'Rice' should exist"));


        assertEquals(5, rice.getQuantity());
    }

    @Test
    void shouldNotAllowQuantityDecreaseBelowZero() {
        Pantry pantry = new Pantry();
        pantry.addProduct("Rice", Category.GRAINS);
        pantry.increaseQuantity("Rice", 1);

        assertThrows(InsufficientStockException.class,
                () -> pantry.decreaseQuantity("Rice", 5));
    }

    @Test
    void shouldThrowWhenDecreasingQuantityOfNonExistingProduct() {
        Pantry pantry = new Pantry();

        assertThrows(ProductNotFoundException.class,
                () -> pantry.decreaseQuantity("Rice", 3));
    }

    @Test
    void shouldNotAllowNonPositiveDecrease() {
        Pantry pantry = new Pantry();
        pantry.addProduct("Rice", Category.GRAINS);
        pantry.increaseQuantity("Rice", 10);

        assertThrows(IllegalArgumentException.class,
                () -> pantry.decreaseQuantity("Rice", 0));
        assertThrows(IllegalArgumentException.class,
                () -> pantry.decreaseQuantity("Rice", -3));
    }

}
