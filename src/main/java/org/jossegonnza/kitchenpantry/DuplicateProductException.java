package org.jossegonnza.kitchenpantry;

public class DuplicateProductException extends RuntimeException {
    public DuplicateProductException(String productName) {
        super("Product %s already exists in pantry".formatted(productName));
    }
}
