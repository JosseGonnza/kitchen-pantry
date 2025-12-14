package org.jossegonnza.kitchenpantry.infrastructure.persistence.jdbc.mapper;

import org.jossegonnza.kitchenpantry.domain.Category;
import org.jossegonnza.kitchenpantry.domain.Product;
import org.jossegonnza.kitchenpantry.infrastructure.persistence.jdbc.entity.ProductEntity;

import java.time.LocalDateTime;

public class ProductMapper {
    public static ProductEntity toEntity(Product product, Long id) {
        return new ProductEntity(
                id,
                product.getName(),
                product.getCategory().name(),
                product.getQuantity(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public static Product toDomain(ProductEntity entity) {
        Product product = new Product(
                entity.getName(),
                Category.valueOf(entity.getCategory())
        );
        // Reconstruimos la cantidad de quantity desde la entidad
        int currentQuantity = entity.getQuantity();
        if (currentQuantity > 0) {
            product.increaseQuantity(currentQuantity);
        }
        return product;
    }
}
