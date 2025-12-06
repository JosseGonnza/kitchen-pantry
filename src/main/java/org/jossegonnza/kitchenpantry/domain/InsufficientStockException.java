package org.jossegonnza.kitchenpantry.domain;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String productName) {
        super("Product '%s' has insufficient stock".formatted(productName));
    }
}
