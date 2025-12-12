package org.jossegonnza.kitchenpantry.api.product.dto;

import jakarta.validation.constraints.Positive;

public record ConsumeProductRequest(
        @Positive int amount
) {}
