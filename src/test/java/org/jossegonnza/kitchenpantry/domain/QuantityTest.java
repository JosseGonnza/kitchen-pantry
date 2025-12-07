package org.jossegonnza.kitchenpantry.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class QuantityTest {
    @Test
    void shouldCreateQuantityWithZeroOrPositiveValue() {
        Quantity zero = new Quantity(0);
        Quantity five = new Quantity(5);

        assertEquals(0, zero.value());
        assertEquals(5, five.value());
    }

    @Test
    void shouldNotAllowNegativeInitialValue() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity(-1));
    }

    @Test
    void shouldIncreaseQuantityByPositiveAmount() {
        Quantity quantity = new Quantity(5);

        Quantity result = quantity.add(3);

        assertEquals(8, result.value());
    }

    @Test
    void shouldNotAllowNonPositiveIncrease() {
        Quantity quantity = new Quantity(5);

        assertThrows(IllegalArgumentException.class,
                () -> quantity.add(0));
        assertThrows(IllegalArgumentException.class,
                () -> quantity.add(-2));
    }

    @Test
    void shouldDecreaseQuantityByPositiveAmount() {
        Quantity quantity = new Quantity(10);

        Quantity result = quantity.subtract(4);

        assertEquals(6, result.value());
    }
}
