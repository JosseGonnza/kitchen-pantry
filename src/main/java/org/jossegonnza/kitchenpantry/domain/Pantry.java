package org.jossegonnza.kitchenpantry.domain;

import org.jossegonnza.kitchenpantry.domain.exception.DuplicateProductException;
import org.jossegonnza.kitchenpantry.domain.exception.ProductNotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
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

        batches.add(batch);
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

    // Helpers
    private boolean hasProduct(String productName) {
        return findByName(productName).isPresent();
    }
}
