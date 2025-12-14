package org.jossegonnza.kitchenpantry.infrastructure.persistence.jdbc;

import org.jossegonnza.kitchenpantry.domain.Batch;
import org.jossegonnza.kitchenpantry.domain.Product;
import org.jossegonnza.kitchenpantry.domain.exception.DuplicateProductException;
import org.jossegonnza.kitchenpantry.domain.exception.ProductNotFoundException;
import org.jossegonnza.kitchenpantry.infrastructure.persistence.jdbc.entity.BatchEntity;
import org.jossegonnza.kitchenpantry.infrastructure.persistence.jdbc.entity.ProductEntity;
import org.jossegonnza.kitchenpantry.infrastructure.persistence.jdbc.mapper.BatchMapper;
import org.jossegonnza.kitchenpantry.infrastructure.persistence.jdbc.mapper.ProductMapper;
import org.jossegonnza.kitchenpantry.infrastructure.persistence.jdbc.repository.SpringDataBatchRepository;
import org.jossegonnza.kitchenpantry.infrastructure.persistence.jdbc.repository.SpringDataProductRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Repository
public class JdbcPantryRepository {
    private final SpringDataProductRepository productRepository;
    private final SpringDataBatchRepository batchRepository;

    public JdbcPantryRepository(SpringDataProductRepository productRepository,
                                SpringDataBatchRepository batchRepository) {
        this.productRepository = productRepository;
        this.batchRepository = batchRepository;
    }

    // PRODUCTS

    @Transactional
    public void saveProduct(Product product) {
        if (productRepository.existsByNameIgnoreCase(product.getName())) {
            throw new DuplicateProductException(product.getName());
        }
        ProductEntity entity = ProductMapper.toEntity(product, null);
        productRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public Optional<Product> findProductByName(String name) {
        return productRepository.findByNameIgnoreCase(name)
                .map(ProductMapper::toDomain);
    }

    @Transactional(readOnly = true)
    public List<Product> findAllProducts() {
        return StreamSupport.stream(productRepository.findAll().spliterator(), false)
                .map(ProductMapper::toDomain)
                .toList();
    }

    @Transactional
    public void updateProduct(Product product) {
        ProductEntity existing = productRepository.findByNameIgnoreCase(product.getName())
                .orElseThrow(() -> new ProductNotFoundException(product.getName()));

        ProductEntity updated = ProductMapper.toEntity(product, existing.getId());
        updated.setCreatedAt(existing.getCreatedAt());
        productRepository.save(updated);
    }

    @Transactional
    public void deleteProduct(String productName) {
        ProductEntity entity = productRepository.findByNameIgnoreCase(productName)
                .orElseThrow(() -> new ProductNotFoundException(productName));

        productRepository.deleteById(entity.getId());
    }

    //BATCHES

    @Transactional
    public void saveBatch(Batch batch, String productName) {
        ProductEntity productEntity = productRepository.findByNameIgnoreCase(productName)
                .orElseThrow(() -> new ProductNotFoundException(productName));

        BatchEntity batchEntity = BatchMapper.toEntity(batch, productEntity.getId());
        batchRepository.save(batchEntity);
    }

    @Transactional(readOnly = true)
    public List<Batch> findBatchesByProductName(String productName) {
        ProductEntity productEntity = productRepository.findByNameIgnoreCase(productName)
                .orElseThrow(() -> new ProductNotFoundException(productName));

        return batchRepository.findByProductIdOrderByExpiryDateAsc(productEntity.getId())
                .stream()
                .map(entity -> BatchMapper.toDomain(entity, productName))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Batch> findExpiredBatches(String productName, LocalDate date) {
        ProductEntity productEntity = productRepository.findByNameIgnoreCase(productName)
                .orElseThrow(() -> new ProductNotFoundException(productName));

        return batchRepository.findExpiredBatches(productEntity.getId(), date)
                .stream()
                .map(entity -> BatchMapper.toDomain(entity, productName))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Batch> findBatchesExpiringWithin(String productName, int days) {
        ProductEntity productEntity = productRepository.findByNameIgnoreCase(productName)
                .orElseThrow(() -> new ProductNotFoundException(productName));

        LocalDate limitDate = LocalDate.now().plusDays(days);
        return batchRepository.findBatchesExpiringBefore(productEntity.getId(), limitDate)
                .stream()
                .map(entity -> BatchMapper.toDomain(entity, productName))
                .toList();
    }

    @Transactional
    public void deleteBatch(Long batchId) {
        batchRepository.deleteById(batchId);
    }
}
