package org.jossegonnza.kitchenpantry.domain.summary;

import org.jossegonnza.kitchenpantry.domain.Category;
import org.jossegonnza.kitchenpantry.domain.Pantry;
import org.jossegonnza.kitchenpantry.domain.StockSummary;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LowStockTest {
    @Test
    void shouldReturnProductsBelowGivenQuantityThreshold() {
        Pantry pantry = new Pantry();
        pantry.addProduct("Rice", Category.GRAINS);
        pantry.addProduct("Chicken", Category.MEAT);
        pantry.addProduct("Parsley", Category.OTHER);

        pantry.addBatch("Rice", 5, LocalDate.of(2025, 12, 1));
        pantry.addBatch("Rice", 3, LocalDate.of(2025, 12, 10));
        pantry.addBatch("Chicken", 2, LocalDate.of(2025, 11, 20));
        pantry.addBatch("Parsley", 15, LocalDate.of(2025, 12, 5));

        List<StockSummary> lowStock = pantry.getProductsBelowQuantity(5);

        assertEquals(1, lowStock.size());
        assertEquals("Chicken", lowStock.getFirst().productName());
        assertEquals(2, lowStock.getFirst().totalQuantity());
    }

    @Test
    void shouldReturnLowStockProductsFilterByCategory() {
        Pantry pantry = new Pantry();
        pantry.addProduct("Rice", Category.GRAINS);
        pantry.addProduct("Chicken", Category.MEAT);
        pantry.addProduct("Pasta", Category.GRAINS);

        pantry.addBatch("Rice", 3, LocalDate.of(2025, 12, 1));
        pantry.addBatch("Pasta", 10, LocalDate.of(2025, 12, 10));
        pantry.addBatch("Chicken", 2, LocalDate.of(2025, 11, 20));

        List<StockSummary> lowGrains = pantry.getProductsBelowQuantity(Category.GRAINS, 5);

        assertEquals(1, lowGrains.size());
        assertEquals("Rice", lowGrains.getFirst().productName());
        assertEquals(Category.GRAINS, lowGrains.getFirst().category());
        assertEquals(3, lowGrains.getFirst().totalQuantity());
    }
}