package org.jossegonnza.kitchenpantry.infrastructure.persistence.jdbc.repository;

import org.jossegonnza.kitchenpantry.infrastructure.persistence.jdbc.entity.BatchEntity;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SpringDataBatchRepository extends CrudRepository<BatchEntity, Long> {
    List<BatchEntity> findByProductInOrderByExpiryDateAsc(Long productId);

    @Query("SELECT * FROM batches WHERE product_id = :productId AND expiry_date <= :date ORDER BY expiry_date ASC")
    List<BatchEntity> findExpiredBatches(@Param("productId") Long productId, @Param("date")LocalDate date);

    @Query("SELECT * FROM batches WHERE product_id = :productId AND expiry_date <= :limitDate ORDER BY expiry_date ASC")
    List<BatchEntity> findBatchesExpiringBefore(@Param("productId") Long productId, @Param("limitDate") LocalDate limitDate);
}
