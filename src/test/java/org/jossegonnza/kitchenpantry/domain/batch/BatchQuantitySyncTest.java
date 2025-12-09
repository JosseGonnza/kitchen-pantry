package org.jossegonnza.kitchenpantry.domain.batch;

import org.jossegonnza.kitchenpantry.domain.Category;
import org.jossegonnza.kitchenpantry.domain.Pantry;
import org.jossegonnza.kitchenpantry.domain.Product;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BatchQuantitySyncTest {
    @Test
    void shouldIncreaseProductQuantityWhenAddingBatch() {
        Pantry pantry = new Pantry();
        pantry.addProduct("Rice", Category.GRAINS);

        pantry.addBatch("Rice", 5, LocalDate.of(2025, 12, 31));
        Product rice = pantry.findByName("Rice")
                .orElseThrow(() -> new AssertionError("Product should exist"));

        assertEquals(5, rice.getQuantity());
    }

    @Test
    void shouldAccumulateQuantityWhenAddingMultipleBatches() {
        Pantry pantry = new Pantry();
        pantry.addProduct("Rice", Category.GRAINS);

        pantry.addBatch("Rice", 5, LocalDate.of(2025, 12, 31));
        pantry.addBatch("Rice", 3, LocalDate.of(2026, 1, 15));
        Product rice = pantry.findByName("Rice")
                .orElseThrow(() -> new AssertionError("Product should exist"));

        assertEquals(8, rice.getQuantity());
    }
}
