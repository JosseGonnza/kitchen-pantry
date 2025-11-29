package org.jossegonnza.pantry;

import java.util.ArrayList;
import java.util.List;

public class Pantry {
    private final List<Product> products = new ArrayList<>();

    public void addProduct(String productName) {
        boolean productAlreadyExists = hasProductNamed(productName);
        if (!productAlreadyExists) products.add(new Product(productName));
    }

    private boolean hasProductNamed(String productName) {
        return products
                .stream()
                .anyMatch(p -> p.getName().equals(productName));
    }

    public List<Product> getProducts() {
        return this.products;
    }
}
