package org.jossegonnza.kitchenpantry.application;

import org.jossegonnza.kitchenpantry.domain.Category;
import org.jossegonnza.kitchenpantry.domain.Pantry;
import org.jossegonnza.kitchenpantry.domain.Product;

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
}
