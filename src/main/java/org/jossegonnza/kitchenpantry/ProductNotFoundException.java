package org.jossegonnza.kitchenpantry;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String productName) {
        super("Product '%s' not found in pantry".formatted(productName));
    }
}
