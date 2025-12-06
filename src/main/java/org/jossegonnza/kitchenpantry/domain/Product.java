package org.jossegonnza.kitchenpantry.domain;

public class Product {
    private final String name;
    private final Category category;
    private int quantity;

    public Product(String name, Category category) {
        this.name = name;
        this.category = category;
        this.quantity = 0;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void increaseQuantity(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount to increase must be positive");
        }
        this.quantity += amount;
    }

    public void decreaseQuantity(int amount) {
        this.quantity -= amount;
    }
}
