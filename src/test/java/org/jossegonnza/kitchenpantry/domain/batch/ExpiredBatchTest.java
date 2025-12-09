package org.jossegonnza.kitchenpantry.domain.batch;

import org.jossegonnza.kitchenpantry.domain.Batch;
import org.jossegonnza.kitchenpantry.domain.Category;
import org.jossegonnza.kitchenpantry.domain.Pantry;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExpiredBatchTest {
    @Test
    void shouldReturnExpiredBatches() {
        Pantry pantry = new Pantry();
        pantry.addProduct("Milk", Category.DAIRY);

        pantry.addBatch("Milk", 3, LocalDate.now().minusDays(1));
        pantry.addBatch("Milk", 5, LocalDate.now().plusDays(5));
        List<Batch> expired = pantry.getExpiredBatches("Milk");

        assertEquals(1, expired.size());
        assertEquals(3, expired.getFirst().quantity().value());
    }
}
