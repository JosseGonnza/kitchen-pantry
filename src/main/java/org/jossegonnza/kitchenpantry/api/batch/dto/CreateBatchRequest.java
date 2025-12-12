package org.jossegonnza.kitchenpantry.api.batch.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record CreateBatchRequest(
        @Positive int amount,
        @NotNull LocalDate expiryDate
) {}
