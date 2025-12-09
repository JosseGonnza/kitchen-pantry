package org.jossegonnza.kitchenpantry.domain.batch;

import org.jossegonnza.kitchenpantry.domain.Batch;
import org.jossegonnza.kitchenpantry.domain.Category;
import org.jossegonnza.kitchenpantry.domain.Pantry;
import org.jossegonnza.kitchenpantry.domain.Product;
import org.jossegonnza.kitchenpantry.domain.exception.InsufficientStockException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BatchConsumptionTest {
    @Test
    void shouldConsumeFromSingleBatchAndSyncProductQuantity() {
        Pantry pantry = new Pantry();
        pantry.addProduct("Rice", Category.GRAINS);
        pantry.addBatch("Rice", 10, LocalDate.of(2025, 12, 1));

        pantry.consumeProduct("Rice", 6);
        List<Batch> batches = pantry.getBatches("Rice");

        assertEquals(1, batches.size());
        assertEquals(4, batches.getFirst().quantity().value());
        Product rice = pantry.findByName("Rice")
                .orElseThrow(() -> new AssertionError("Product 'Rice' should exist"));
        assertEquals(4, rice.getQuantity());
    }
    @Test
    void shouldThrowWhenConsumingMoreThanSingleBatchQuantity() {
        Pantry pantry = new Pantry();
        pantry.addProduct("Rice", Category.GRAINS);
        pantry.addBatch("Rice", 5, LocalDate.of(2025, 12, 1));

        assertThrows(InsufficientStockException.class,
                () -> pantry.consumeProduct("Rice", 10));
    }

}
