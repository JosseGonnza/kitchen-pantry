package org.jossegonnza.kitchenpantry.domain.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String productName) {
        super("Product '%s' not found in pantry".formatted(productName));
    }
}
