package org.jossegonnza.kitchenpantry.domain.summary;

import org.jossegonnza.kitchenpantry.application.PantryService;
import org.jossegonnza.kitchenpantry.domain.Category;
import org.jossegonnza.kitchenpantry.domain.Pantry;
import org.jossegonnza.kitchenpantry.domain.StockSummary;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class StockSummaryTest {
    @Test
    void shouldBuildStockSummaryForProductWithBatches() {
        Pantry pantry = new Pantry();
        pantry.addProduct("Rice", Category.GRAINS);
        pantry.addProduct("Chicken", Category.MEAT);

        pantry.addBatch("Rice", 5, LocalDate.of(2026, 1, 1));
        pantry.addBatch("Rice", 3, LocalDate.of(2026, 2, 10));
        pantry.addBatch("Chicken", 4, LocalDate.of(2026, 2, 6));
        List<StockSummary> summaries = pantry.getStockSummary();

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

    @Test
    void shouldBuildStockSummaryForProductWithoutBatches() {
        Pantry pantry = new Pantry();
        pantry.addProduct("Rice", Category.GRAINS);

        List<StockSummary> summaries = pantry.getStockSummary();

        StockSummary rice = summaries.stream()
                .filter(s -> s.productName().equals("Rice"))
                .findFirst()
                .orElseThrow();

        assertEquals(0, rice.totalQuantity());
        assertEquals(0, rice.numberOfBatches());
        assertNull(rice.nextExpiryDate());
    }

    @Test
    void shouldReturnProductsBelowGivenQuantityThreshold() {
        PantryService service = new PantryService(new Pantry());
        service.addProduct("Rice", Category.GRAINS);
        service.addProduct("Chicken", Category.MEAT);
        service.addProduct("Parsley", Category.OTHER);

        service.addBatch("Rice", 5, LocalDate.of(2025, 12, 1));
        service.addBatch("Rice", 3, LocalDate.of(2025, 12, 10));
        service.addBatch("Chicken", 2, LocalDate.of(2025, 11, 20));
        service.addBatch("Parsley", 15, LocalDate.of(2025, 12, 5));

        List<StockSummary> lowStock = service.getProductsBelowQuantity(5);

        assertEquals(1, lowStock.size());
        assertEquals("Chicken", lowStock.getFirst().productName());
        assertEquals(2, lowStock.getFirst().totalQuantity());
    }

    @Test
    void shouldReturnLowStockProductsFilterByCategory() {
        PantryService service = new PantryService(new Pantry());
        service.addProduct("Rice", Category.GRAINS);
        service.addProduct("Chicken", Category.MEAT);
        service.addProduct("Pasta", Category.GRAINS);

        service.addBatch("Rice", 3, LocalDate.of(2025, 12, 1));
        service.addBatch("Pasta", 10, LocalDate.of(2025, 12, 10));
        service.addBatch("Chicken", 2, LocalDate.of(2025, 11, 20));

        List<StockSummary> lowGrains = service.getProductsBelowQuantity(Category.GRAINS, 5);

        assertEquals(1, lowGrains.size());
        assertEquals("Rice", lowGrains.getFirst().productName());
        assertEquals(Category.GRAINS, lowGrains.getFirst().category());
        assertEquals(3, lowGrains.getFirst().totalQuantity());
    }
}
