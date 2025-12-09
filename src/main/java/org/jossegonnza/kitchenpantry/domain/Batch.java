package org.jossegonnza.kitchenpantry.domain;

import org.jossegonnza.kitchenpantry.domain.exception.InsufficientStockException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Batch implements Comparable<Batch> {
    private final ProductName productName;
    private Quantity quantity;
    private final LocalDate expiryDate;
    private final LocalDateTime createdAt;

    public Batch(ProductName productName, Quantity quantity, LocalDate expiryDate) {
        if (productName == null) {
            throw new IllegalArgumentException("Product name cannot be null");
        }
        if (quantity == null) {
            throw new IllegalArgumentException("Quantity cannot be null");
        }
        if (expiryDate == null) {
            throw new IllegalArgumentException("Expiry date cannot be null");
        }
        this.productName = productName;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
        this.createdAt = LocalDateTime.now();
    }

    public void consume(int amount) {
        int available = quantity.value();
        if (amount > available) {
            throw new InsufficientStockException(productName.value(), amount, available);
        }
        this.quantity = this.quantity.subtract(amount);
    }

    public ProductName productName() {
        return productName;
    }

    public Quantity quantity() {
        return quantity;
    }

    public LocalDate expiryDate() {
        return expiryDate;
    }

    public LocalDateTime createdAt() {
        return createdAt;
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

    @Override
    public int compareTo(Batch other) {
        int byExpiry = this.expiryDate.compareTo(other.expiryDate);
        if (byExpiry != 0) {
            return byExpiry;
        } else {
            return this.createdAt.compareTo(other.createdAt());
        }
    }

    public boolean isExpiredAt(LocalDate date) {
        return expiryDate.isBefore(date) || expiryDate.isEqual(date);
    }
}
