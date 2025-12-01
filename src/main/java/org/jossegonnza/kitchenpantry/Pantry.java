package org.jossegonnza.kitchenpantry;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Pantry {
    private final List<Product> products = new ArrayList<>();

    public void addProduct(String productName, Category category) {
        boolean productAlreadyExists = hasProduct(productName);
        if (productAlreadyExists) {
            throw new DuplicateProductException(productName);
        } else {
            products.add(new Product(productName, category));
        }
    }

    public void addProduct(String name) {
        addProduct(name, Category.OTHER);
    }

    public void deleteProduct(String productName) {
        boolean removed = products.removeIf(product -> hasName(productName, product));
        if (!removed) throw new ProductNotFoundException(productName);
    }

    public Optional<Product> findByName(String productName) {
        return products
                .stream()
                .filter(product -> hasName(productName, product))
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

    private boolean hasProduct(String productName) {
        return findByName(productName).isPresent();
    }

    private static boolean hasName(String productName, Product product) {
        return product.getName().equals(productName);
    }
}
