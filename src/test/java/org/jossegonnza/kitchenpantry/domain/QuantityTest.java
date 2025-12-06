package org.jossegonnza.kitchenpantry.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuantityTest {
    @Test
    void shouldCreateQuantityWithZeroOrPositiveValue() {
        Quantity zero = new Quantity(0);
        Quantity five = new Quantity(5);

        assertEquals(0, zero.value());
        assertEquals(5, five.value());
    }
}
