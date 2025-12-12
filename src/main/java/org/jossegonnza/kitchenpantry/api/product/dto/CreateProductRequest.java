package org.jossegonnza.kitchenpantry.api.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.jossegonnza.kitchenpantry.domain.Category;

public record CreateProductRequest(
        @NotBlank String name,
        @NotNull Category category
) {}
