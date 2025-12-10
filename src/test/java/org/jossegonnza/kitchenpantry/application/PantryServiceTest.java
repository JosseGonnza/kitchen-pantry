package org.jossegonnza.kitchenpantry.application;

import org.jossegonnza.kitchenpantry.domain.Batch;
import org.jossegonnza.kitchenpantry.domain.Category;
import org.jossegonnza.kitchenpantry.domain.Pantry;
import org.jossegonnza.kitchenpantry.domain.Product;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PantryServiceTest {
    @Test
    void shouldAddNewProductToPantry() {
        PantryService service = new PantryService(new Pantry());

        service.addProduct("Rice", Category.GRAINS);
        List<Product> products = service.getAllProducts();

        assertEquals(1, products.size());
        assertEquals("Rice", products.getFirst().getName());
        assertEquals(Category.GRAINS, products.getFirst().getCategory());
    }

    @Test
    void shouldAddNewBatchToPantry() {
        PantryService service = new PantryService(new Pantry());
        service.addProduct("Rice", Category.GRAINS);

        service.addBatch("Rice", 5, LocalDate.of(2025, 12, 1));
        List<Batch> batches = service.getAllBatches("Rice");

        assertEquals(1, batches.size());
        assertEquals("Rice", batches.getFirst().productName().value());
        assertEquals(5, batches.getFirst().quantity().value());
        assertEquals(LocalDate.of(2025, 12, 1), batches.getFirst().expiryDate());
    }

    @Test
    void shouldConsumeProductOfPantry() {
        PantryService service = new PantryService(new Pantry());
        service.addProduct("Rice", Category.GRAINS);

        service.addBatch("Rice", 5, LocalDate.of(2025, 12, 1));
        service.addBatch("Rice", 8, LocalDate.of(2025, 12, 5));

        service.consumeProduct("Rice", 7);
        List<Batch> batches = service.getAllBatches("Rice");

        assertEquals(1, batches.size());
        assertEquals("Rice", batches.getFirst().productName().value());
        assertEquals(6, batches.getFirst().quantity().value());
        assertEquals(LocalDate.of(2025, 12, 5), batches.getFirst().expiryDate());
    }
}
