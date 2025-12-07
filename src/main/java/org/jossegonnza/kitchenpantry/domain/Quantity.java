package org.jossegonnza.kitchenpantry.domain;

import org.jossegonnza.kitchenpantry.domain.exception.InsufficientStockException;

public class Quantity {
    private int value;

    public Quantity(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Quantity only allow positive amount");
        }
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public Quantity add(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount to increase must be positive");
        }
        return new Quantity(this.value + amount);
    }

    public Quantity subtract(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount to decrease must be positive");
        }
        if (amount > this.value) {
            throw new InsufficientStockException("Unknow product", amount, this.value);
        }
        return new Quantity(this.value - amount);
    }
}
