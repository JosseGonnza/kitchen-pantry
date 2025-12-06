package org.jossegonnza.kitchenpantry.domain;

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
        return new Quantity(this.value + amount);
    }
}
