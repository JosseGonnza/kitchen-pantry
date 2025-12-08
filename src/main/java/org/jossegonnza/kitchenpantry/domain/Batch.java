package org.jossegonnza.kitchenpantry.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Batch {
    private final String productName;
    private final Quantity quantity;
    private final LocalDate expiryDate;

    public Batch(String productName, Quantity quantity, LocalDate expiryDate) {
        if (productName == null || productName.isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        this.productName = productName;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
    }

    public String productName() {
        return productName;
    }

    public Quantity quantity() {
        return quantity;
    }

    public LocalDate expiryDate() {
        return expiryDate;
    }
}
