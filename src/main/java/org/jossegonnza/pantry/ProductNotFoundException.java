package org.jossegonnza.pantry;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String productName) {
        super("Product '%s' not found in pantry".formatted(productName));
    }
}
