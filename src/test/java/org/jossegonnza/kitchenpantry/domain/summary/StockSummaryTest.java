package org.jossegonnza.kitchenpantry.domain.summary;

import org.jossegonnza.kitchenpantry.domain.Category;
import org.jossegonnza.kitchenpantry.domain.Pantry;
import org.jossegonnza.kitchenpantry.domain.StockSummary;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
