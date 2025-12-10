package org.jossegonnza.kitchenpantry.application;

import org.jossegonnza.kitchenpantry.domain.Batch;
import org.jossegonnza.kitchenpantry.domain.Category;
import org.jossegonnza.kitchenpantry.domain.Pantry;
import org.jossegonnza.kitchenpantry.domain.Product;

import java.time.LocalDate;
import java.util.List;

public class PantryService {
    private final Pantry pantry;

    public PantryService(Pantry pantry) {
        this.pantry = pantry;
    }

    public void addProduct(String productName, Category category) {
        pantry.addProduct(productName, category);
    }

    public List<Product> getAllProducts() {
        return pantry.getProducts();
    }

    public void addBatch(String productName, int amount, LocalDate expiryDate) {
        pantry.addBatch(productName, amount, expiryDate);
    }

    public List<Batch> getAllBatches(String productName) {
        return pantry.getBatches(productName);
    }

    public void consumeProduct(String productName, int amount) {
        pantry.consumeProduct(productName, amount);
    }
}
