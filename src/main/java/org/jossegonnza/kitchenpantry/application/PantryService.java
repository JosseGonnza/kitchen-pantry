package org.jossegonnza.kitchenpantry.application;

import org.jossegonnza.kitchenpantry.domain.*;
import org.jossegonnza.kitchenpantry.infrastructure.logging.LogHelper;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PantryService {
    private static final Logger log = LogHelper.getLogger(PantryService.class);
    private final Pantry pantry;

    public PantryService(Pantry pantry) {
        this.pantry = pantry;
    }

    public void addProduct(String productName, Category category) {
        log.info("Adding new product: {} with category: {}", productName, category);
        pantry.addProduct(productName, category);
        log.debug("Product added to pantry: {}", productName);
    }

    public List<Product> getProducts() {
        return pantry.getProducts();
    }

    public void addBatch(String productName, int amount, LocalDate expiryDate) {
        pantry.addBatch(productName, amount, expiryDate);
    }

    public List<Batch> getBatches(String productName) {
        return pantry.getBatches(productName);
    }

    public void consumeProduct(String productName, int amount) {
        pantry.consumeProduct(productName, amount);
    }

    public List<StockSummary> getStockSummary() {
        return pantry.getStockSummary();
    }

    public List<StockSummary> getProductsBelowQuantity(int threshold) {
        return pantry.getProductsBelowQuantity(threshold);
    }

    public List<StockSummary> getProductsBelowQuantity(Category category, int threshold) {
        return pantry.getProductsBelowQuantity(category, threshold);
    }
}
