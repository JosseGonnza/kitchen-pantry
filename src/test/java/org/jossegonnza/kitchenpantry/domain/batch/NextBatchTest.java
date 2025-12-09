package org.jossegonnza.kitchenpantry.domain.batch;

import org.jossegonnza.kitchenpantry.domain.Batch;
import org.jossegonnza.kitchenpantry.domain.Category;
import org.jossegonnza.kitchenpantry.domain.Pantry;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NextBatchTest {
    @Test
    void shouldReturnNextBatchToConsume() {
        Pantry pantry = new Pantry();
        pantry.addProduct("Rice", Category.GRAINS);
        pantry.addBatch("Rice", 5, LocalDate.of(2025, 12, 31));
        pantry.addBatch("Rice", 3, LocalDate.of(2025, 12, 30));

        Batch nextBatch = pantry.getNextBatch("Rice");

        assertEquals(LocalDate.of(2025, 12, 30), nextBatch.expiryDate());
    }

    @Test
    void shouldThrowWhenProductHasNoBatches() {
        Pantry pantry = new Pantry();
        pantry.addProduct("Rice", Category.GRAINS);

        assertThrows(IllegalStateException.class,
                () -> pantry.getNextBatch("Rice"));
    }
}
