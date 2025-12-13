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
        Product product = getExistingProduct(productName);
        product.increaseQuantity(amount);
    }

    public void decreaseQuantity(String productName, int amount) {
        Product product = getExistingProduct(productName);
        product.decreaseQuantity(amount);
    }


    // Batches
    public void addBatch(String productName, int amount, LocalDate expiryDate) {
        Product product = getExistingProduct(productName);
        if (amount <= 0) {
            throw new IllegalArgumentException("Batch amount must be positive");
        }
        Batch batch = new Batch(product.getProductName(), new Quantity(amount), expiryDate);

        batches.add(batch);
        batches.sort(Batch::compareTo);

        product.increaseQuantity(amount);
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
        validateConsumptionAmount(amount);
        Product product = getExistingProduct(productName);
        ProductName name = new ProductName(productName);

        List<Batch> productBatches = getBatchesForProductSortedByExpiry(name);

        int totalAvailable = calculateTotalAvailable(productBatches);

        ensureSufficientStock(productName, amount, totalAvailable);

        consumeFromBatches(productBatches, amount);

        product.decreaseQuantity(amount);
    }

    // Helpers
    private void consumeFromBatches(List<Batch> productBatches, int amount) {
        int remainingToConsume = amount;
        List<Batch> toRemove = new ArrayList<>();

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
                toRemove.add(batch);
            }
        }

        batches.removeAll(toRemove);
    }

    private static void ensureSufficientStock(String productName, int request, int available) {
        if (available < request) {
            throw new InsufficientStockException(productName, request, available);
        }
    }

    private static int calculateTotalAvailable(List<Batch> productBatches) {
        return productBatches.stream()
                .mapToInt(batch -> batch.quantity().value())
                .sum();
    }

    private List<Batch> getBatchesForProductSortedByExpiry(ProductName name) {
        return batches.stream()
                .filter(batch -> batch.productName().equals(name))
                .sorted(Comparator.comparing(Batch::expiryDate))
                .toList();
    }

    private Product getExistingProduct(String productName) {
        return findByName(productName).orElseThrow(() -> new ProductNotFoundException(productName));
    }

    private static void validateConsumptionAmount(int amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount to consume must be positive");
    }

    private boolean hasProduct(String productName) {
        return findByName(productName).isPresent();
    }

    public List<StockSummary> getStockSummary() {
        return products.stream()
                .map(product -> {
                    String name = product.getName();
                    List<Batch> productBatches = getBatches(name);
                    int numberOfBatches = productBatches.size();
                    LocalDate nextExpiryDate = productBatches.isEmpty()
                            ? null
                            : productBatches.get(0).expiryDate();

                    return new StockSummary(
                            name,
                            product.getCategory(),
                            product.getQuantity(),
                            numberOfBatches,
                            nextExpiryDate
                    );
                })
                .toList();
    }

    public List<StockSummary> getProductsBelowQuantity(int threshold) {
        if (threshold < 0) {
            throw new IllegalArgumentException("Threshold must be non-negative");
        }
        return getStockSummary().stream()
                .filter(summary -> summary.totalQuantity() <= threshold)
                .toList();
    }

    public List<StockSummary> getProductsBelowQuantity(Category category, int threshold) {
        if (threshold < 0) {
            throw new IllegalArgumentException("Threshold must be non-negative");
        }
        return getStockSummary().stream()
                .filter(summary -> summary.category() == category)
                .filter(summary -> summary.totalQuantity() <= threshold)
                .toList();
    }
}
