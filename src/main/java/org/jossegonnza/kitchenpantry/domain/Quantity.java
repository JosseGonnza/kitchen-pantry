package org.jossegonnza.kitchenpantry.domain;

public class Quantity {
    private int quantity;

    public Quantity(int quantity) {
        this.quantity = quantity;
    }

    public int value() {
        return this.quantity;
    }
}
