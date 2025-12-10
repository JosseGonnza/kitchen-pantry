package org.jossegonnza.kitchenpantry.application;

import org.jossegonnza.kitchenpantry.domain.*;
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
        assertEquals("Rice", products.get(0).getName());
        assertEquals(Category.GRAINS, products.get(0).getCategory());
    }

    @Test
    void shouldAddNewBatchToPantry() {
        PantryService service = new PantryService(new Pantry());
        service.addProduct("Rice", Category.GRAINS);

        service.addBatch("Rice", 5, LocalDate.of(2025, 12, 1));
        List<Batch> batches = service.getAllBatches("Rice");

        assertEquals(1, batches.size());
        assertEquals("Rice", batches.get(0).productName().value());
        assertEquals(5, batches.get(0).quantity().value());
        assertEquals(LocalDate.of(2025, 12, 1), batches.get(0).expiryDate());
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
        assertEquals("Rice", batches.get(0).productName().value());
        assertEquals(6, batches.get(0).quantity().value());
        assertEquals(LocalDate.of(2025, 12, 5), batches.get(0).expiryDate());
    }

    @Test
    void shouldBuildStockSummary() {
        PantryService service = new PantryService(new Pantry());
        service.addProduct("Rice", Category.GRAINS);
        service.addProduct("Chicken", Category.MEAT);

        service.addBatch("Rice", 5, LocalDate.of(2026, 1, 1));
        service.addBatch("Rice", 3, LocalDate.of(2026, 2, 10));
        service.addBatch("Chicken", 4, LocalDate.of(2026, 2, 6));
        List<StockSummary> summaries = service.getStockSummary();

        StockSummary riceSummary = summaries.stream()
                .filter(stockSummary -> stockSummary.productName().equals("Rice"))
                .findFirst()
                .orElseThrow();

        assertEquals("Rice", riceSummary.productName());
        assertEquals(Category.GRAINS, riceSummary.category());
        assertEquals(8, riceSummary.totalQuantity());
        assertEquals(2, riceSummary.numberOfBatches());
        assertEquals(LocalDate.of(2026, 1, 1), riceSummary.nextExpiryDate());
    }
}
