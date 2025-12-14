package org.jossegonnza.kitchenpantry.infrastructure.persistence.jdbc.repository;

import org.jossegonnza.kitchenpantry.infrastructure.persistence.jdbc.entity.ProductEntity;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SpringDataProductRepository extends CrudRepository<ProductEntity, Long> {

    @Query("SELECT * FROM products WHERE LOWER(name) = LOWER(:name)")
    Optional<ProductEntity> findByNameIgnoreCase(@Param("name") String name);
    boolean existsByNameIgnoreCase(String name);
}
