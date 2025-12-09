package org.jossegonnza.kitchenpantry.domain;

import java.time.LocalDate;

public record StockSummary(
        String productName,
        Category category,
        int totalQuantity,
        int numberOfBatches,
        LocalDate nextExpiryDate
) {}
