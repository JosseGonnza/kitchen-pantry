package org.jossegonnza.kitchenpantry.domain.batch;

import org.jossegonnza.kitchenpantry.domain.Batch;
import org.jossegonnza.kitchenpantry.domain.Category;
import org.jossegonnza.kitchenpantry.domain.Pantry;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BatchOrderingTest {
    @Test
    void shouldOrderBatchesByExpiryDateAutomatically() {
        Pantry pantry = new Pantry();
        pantry.addProduct("Rice", Category.GRAINS);

        pantry.addBatch("Rice", 3, LocalDate.of(2026, 1, 1));
        pantry.addBatch("Rice", 5, LocalDate.of(2025, 12, 1));

        List<Batch> batches = pantry.getBatches("Rice");

        assertEquals(LocalDate.of(2025, 12, 1), batches.get(0).expiryDate());
        assertEquals(LocalDate.of(2026, 1, 1), batches.get(1).expiryDate());
    }

    @Test
    void shouldOrderByCreationWhenExpiryDateAreTheSame() {
        Pantry pantry = new Pantry();
        pantry.addProduct("Rice", Category.GRAINS);

        pantry.addBatch("Rice", 3, LocalDate.of(2026, 1, 1));
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}
        pantry.addBatch("Rice", 5, LocalDate.of(2026, 1, 1));

        List<Batch> batches = pantry.getBatches("Rice");

        assertEquals(3, batches.get(0).quantity().value());
        assertEquals(5, batches.get(1).quantity().value());
    }
}
