package org.jossegonnza.kitchenpantry.api.stock.dto;

import org.jossegonnza.kitchenpantry.domain.Category;
import org.jossegonnza.kitchenpantry.domain.StockSummary;

import java.time.LocalDate;

public record StockSummaryResponse(
        String productName,
        Category category,
        int totalQuantity,
        int numberOfBatches,
        LocalDate nextExpiryDate
) {
    public static StockSummaryResponse from(StockSummary summary) {
        return new StockSummaryResponse(
                summary.productName(),
                summary.category(),
                summary.totalQuantity(),
                summary.numberOfBatches(),
                summary.nextExpiryDate()
        );
    }
}
