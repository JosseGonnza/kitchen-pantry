package org.jossegonnza.pantry;

import java.util.ArrayList;
import java.util.List;

public class Pantry {
    private final List<Product> products = new ArrayList<>();

    public void add(String product) {
        boolean existProduct = products.stream()
                        .anyMatch(p -> p.getName().equals(product));
        if (!existProduct) products.add(new Product(product));
    }

    public List<Product> getProducts() {
        return this.products;
    }
}
