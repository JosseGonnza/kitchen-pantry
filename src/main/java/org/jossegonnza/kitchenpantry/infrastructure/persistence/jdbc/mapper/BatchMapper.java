package org.jossegonnza.kitchenpantry.infrastructure.persistence.jdbc.mapper;

import org.jossegonnza.kitchenpantry.domain.Batch;
import org.jossegonnza.kitchenpantry.domain.ProductName;
import org.jossegonnza.kitchenpantry.domain.Quantity;
import org.jossegonnza.kitchenpantry.infrastructure.persistence.jdbc.entity.BatchEntity;

import java.time.LocalDateTime;

public class BatchMapper {
    public static BatchEntity toEntity(Batch batch, Long productId) {
        return new BatchEntity(
          null,
          productId,
          batch.quantity().value(),
          batch.expiryDate(),
          LocalDateTime.now()
        );
    }

    public static Batch toDomain(BatchEntity entity, String productName) {
        return new Batch(
                new ProductName(productName),
                new Quantity(entity.getQuantity()),
                entity.getExpiryDate()
        );
    }
}
