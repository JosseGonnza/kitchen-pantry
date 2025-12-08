package org.jossegonnza.kitchenpantry.domain.batch;

import org.jossegonnza.kitchenpantry.domain.Batch;
import org.jossegonnza.kitchenpantry.domain.Category;
import org.jossegonnza.kitchenpantry.domain.Pantry;
import org.jossegonnza.kitchenpantry.domain.exception.ProductNotFoundException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BatchTest {
    @Test
    void shouldAddBatchToExistingProduct() {
        Pantry pantry = new Pantry();
        pantry.addProduct("Rice", Category.GRAINS);

        pantry.addBatch("Rice", 5, LocalDate.of(2025, 12, 31));
        List<Batch> batches = pantry.getBatches("Rice");

        assertEquals(1, batches.size());
        assertEquals("Rice", batches.get(0).productName());
        assertEquals(5, batches.get(0).quantity().value());
        assertEquals(LocalDate.of(2025, 12, 31), batches.get(0).expiryDate());
    }

    @Test
    void shouldThrowWhenAddingBatchToNonExistingProduct() {
        Pantry pantry = new Pantry();

        assertThrows(ProductNotFoundException.class,
                () -> pantry.addBatch("Rice", 5, LocalDate.of(2025, 12, 31)));
    }

    @Test
    void shouldNotAllowNonPositiveBatchAmount() {
        Pantry pantry = new Pantry();
        pantry.addProduct("Rice", Category.GRAINS);

        assertThrows(IllegalArgumentException.class,
                () -> pantry.addBatch("Rice", 0, LocalDate.of(2025, 12, 31)));
        assertThrows(IllegalArgumentException.class,
                () -> pantry.addBatch("Rice", -3, LocalDate.of(2025, 12, 31)));
    }
}
