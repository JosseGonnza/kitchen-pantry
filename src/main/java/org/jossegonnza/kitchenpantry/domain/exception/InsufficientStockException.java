package org.jossegonnza.kitchenpantry.domain.exception;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String productName, int requested, int available) {
        super("Insufficient stock for product '%s'. Requested: %d, available: %d"
                .formatted(productName, requested, available));
    }
}
