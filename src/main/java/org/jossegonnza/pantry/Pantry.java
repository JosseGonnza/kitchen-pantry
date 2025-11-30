package org.jossegonnza.pantry;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Pantry {
    private final List<Product> products = new ArrayList<>();

    public void addProduct(String productName, Category category) {
        boolean productAlreadyExists = hasProduct(productName);
        if (!productAlreadyExists) products.add(new Product(productName, category));
    }

    public void addProduct(String name) {
        addProduct(name, Category.OTHER);
    }

    public void deleteProduct(String productName) {
        products.removeIf(product -> isEquals(productName, product));
    }

    public Optional<Product> findByName(String productName) {
        return products
                .stream()
                .filter(product -> isEquals(productName, product))
                .findFirst();
    }

    private boolean hasProduct(String productName) {
        return products
                .stream()
                .anyMatch(product -> isEquals(productName, product));
    }

    private static boolean isEquals(String productName, Product product) {
        return product.getName().equals(productName);
    }

    public List<Product> getProducts() {
        return this.products;
    }
}
