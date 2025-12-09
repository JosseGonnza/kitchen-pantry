package org.jossegonnza.kitchenpantry.domain.batch;

import org.jossegonnza.kitchenpantry.domain.Batch;
import org.jossegonnza.kitchenpantry.domain.Category;
import org.jossegonnza.kitchenpantry.domain.Pantry;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExpiringSoonBatchTest {
    @Test
    void shouldReturnBatchesExpiringWithinGivenDays() {
        Pantry pantry = new Pantry();
        pantry.addProduct("Milk", Category.DAIRY);

        pantry.addBatch("Milk", 3, LocalDate.now().plusDays(2));
        pantry.addBatch("Milk", 5, LocalDate.now().plusDays(10));

        List<Batch> soon = pantry.getBatchesExpiringWithin("Milk", 5);

        assertEquals(1, soon.size());
        assertEquals(3, soon.getFirst().quantity().value());
    }
}
