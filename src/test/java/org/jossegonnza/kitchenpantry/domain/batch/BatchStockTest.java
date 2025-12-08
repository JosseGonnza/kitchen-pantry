package org.jossegonnza.kitchenpantry.domain.batch;

import org.jossegonnza.kitchenpantry.domain.Category;
import org.jossegonnza.kitchenpantry.domain.Pantry;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BatchStockTest {
    @Test
    void shouldCalculateTotalQuantityFromBatchesForProduct() {
        Pantry pantry = new Pantry();
        pantry.addProduct("Rice", Category.GRAINS);
        pantry.addBatch("Rice", 5, LocalDate.of(2025, 12, 31));
        pantry.addBatch("Rice", 3, LocalDate.of(2026, 1, 1));

        int totalQuantity = pantry.getTotalQuantityFromBatches("Rice");

        assertEquals(8, totalQuantity);
    }

    @Test
    void shouldReturnZeroWhenProductHasNoBatches() {
        Pantry pantry = new Pantry();
        pantry.addProduct("Rice", Category.GRAINS);

        int totalQuantity = pantry.getTotalQuantityFromBatches("Rice");

        assertEquals(0, totalQuantity);
    }
}
