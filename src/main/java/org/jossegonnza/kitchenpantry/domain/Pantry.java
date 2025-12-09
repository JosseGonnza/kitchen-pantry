package org.jossegonnza.kitchenpantry.domain;

import org.jossegonnza.kitchenpantry.domain.exception.DuplicateProductException;
import org.jossegonnza.kitchenpantry.domain.exception.InsufficientStockException;
import org.jossegonnza.kitchenpantry.domain.exception.ProductNotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class Pantry {
    private final List<Product> products = new ArrayList<>();
    private final List<Batch> batches = new ArrayList<>();

    // Products
    public void addProduct(String name, Category category) {
        boolean productAlreadyExists = hasProduct(name);
        if (productAlreadyExists) {
            throw new DuplicateProductException(name);
        } else {
            products.add(new Product(name, category));
        }
    }

    public void addProduct(String name) {
        addProduct(name, Category.OTHER);
    }

    public void deleteProduct(String productName) {
        boolean removed = products.removeIf(product -> product.hasName(productName));
        if (!removed) throw new ProductNotFoundException(productName);
    }

    public Optional<Product> findByName(String productName) {
        return products
                .stream()
                .filter(product -> product.hasName(productName))
                .findFirst();
    }

    public List<Product> getProducts() {
        return List.copyOf(products);
    }

    public List<Product> getProductsByCategory(Category category) {
        return products
                .stream()
                .filter(product -> product.getCategory() == category)
                .toList();
    }

    public void increaseQuantity(String productName, int amount) {
        Product product = findByName(productName)
                .orElseThrow(() -> new ProductNotFoundException(productName));
        product.increaseQuantity(amount);
    }

    public void decreaseQuantity(String productName, int amount) {
        Product product = findByName(productName)
                .orElseThrow(() -> new ProductNotFoundException(productName));
        product.decreaseQuantity(amount);
    }


    // Batches
    public void addBatch(String productName, int amount, LocalDate expiryDate) {
        Product product = findByName(productName)
                .orElseThrow(() -> new ProductNotFoundException(productName));
        if (amount <= 0) {
            throw new IllegalArgumentException("Batch amount must be positive");
        }
        Batch batch = new Batch(product.getProductName(), new Quantity(amount), expiryDate);
        product.increaseQuantity(amount);

        batches.add(batch);
        batches.sort(null);
    }

    public List<Batch> getBatches(String name) {
        return batches.stream()
                .filter(batch -> batch.productName().equals(new ProductName(name)))
                .toList();
    }

    public int getTotalQuantityFromBatches(String name) {
        return batches.stream()
                .filter(batch -> batch.productName().equals(new ProductName(name)))
                .mapToInt(batch -> batch.quantity().value())
                .sum();
    }

    public Batch getNextBatch(String productName) {
        ProductName name = new ProductName(productName);
        return batches.stream()
                .filter(batch -> batch.productName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No batches available for product " + productName));
    }

    public List<Batch> getExpiredBatches(String productName) {
        ProductName name = new ProductName(productName);
        return batches.stream()
                .filter(batch -> batch.productName().equals(name))
                .filter(batch -> batch.isExpiredAt(LocalDate.now()))
                .toList();
    }

    public List<Batch> getBatchesExpiringWithin(String productName, int days) {
        ProductName name = new ProductName(productName);
        LocalDate limit = LocalDate.now().plusDays(days);
        return batches.stream()
                .filter(batch -> batch.productName().equals(name))
                .filter(batch -> batch.expiryDate().isBefore(limit) || batch.expiryDate().isEqual(limit))
                .toList();
    }

    public void consumeProduct(String productName, int amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount to consume must be positive");
        Product product = findByName(productName).orElseThrow(() -> new ProductNotFoundException(productName));
        ProductName name = new ProductName(productName);

        List<Batch> productBatches = batches.stream()
                .filter(batch -> batch.productName().equals(name))
                .sorted(Comparator.comparing(Batch::expiryDate))
                .toList();

        int totalAvailable = productBatches.stream()
                .mapToInt(batch -> batch.quantity().value())
                .sum();

        if (totalAvailable < amount) {
            throw new InsufficientStockException(productName, amount, totalAvailable);
        }

        int remainingToConsume = amount;
        for (Batch batch : productBatches) {
            if (remainingToConsume == 0) {
                break;
            }
            int batchAvailable = batch.quantity().value();
            if (batchAvailable <= remainingToConsume) {
                batch.consume(batchAvailable);
                remainingToConsume -= batchAvailable;
            } else {
                batch.consume(remainingToConsume);
                remainingToConsume = 0;
            }
            if (batch.isEmpty()) {
                batches.remove(batch);
            }
        }

        product.decreaseQuantity(amount);
    }

    // Helpers
    private boolean hasProduct(String productName) {
        return findByName(productName).isPresent();
    }
}
