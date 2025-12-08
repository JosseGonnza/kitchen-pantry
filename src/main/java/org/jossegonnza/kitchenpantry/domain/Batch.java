package org.jossegonnza.kitchenpantry.domain;

import java.time.LocalDate;
import java.util.Objects;

public class Batch {
    private final String productName;
    private final Quantity quantity;
    private final LocalDate expiryDate;

    public Batch(String productName, Quantity quantity, LocalDate expiryDate) {
        if (productName == null || productName.isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
        if (quantity == null) {
            throw new IllegalArgumentException("Quantity cannot be null");
        }
        if (expiryDate == null) {
            throw new IllegalArgumentException("Quantity cannot be null");
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Batch batch = (Batch) o;
        return Objects.equals(productName, batch.productName)
                && Objects.equals(quantity, batch.quantity)
                && Objects.equals(expiryDate, batch.expiryDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productName, quantity, expiryDate);
    }

    @Override
    public String toString() {
        return "Batch{" +
                "productName='" + productName + '\'' +
                ", quantity=" + quantity +
                ", expiryDate=" + expiryDate +
                '}';
    }
}
