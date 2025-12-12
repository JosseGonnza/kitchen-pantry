package org.jossegonnza.kitchenpantry.api.batch.dto;

import java.time.LocalDate;

public record BatchResponse(
        String productName,
        int quantity,
        LocalDate expiryDate
) {}
