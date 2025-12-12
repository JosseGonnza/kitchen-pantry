package org.jossegonnza.kitchenpantry.api.product.dto;

import org.jossegonnza.kitchenpantry.domain.Category;
import org.jossegonnza.kitchenpantry.domain.Product;

public record ProductResponse(
        String name,
        Category category
) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(product.getName(), product.getCategory());
    }
}
