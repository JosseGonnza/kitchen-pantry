package org.jossegonnza.kitchenpantry.domain;

import org.jossegonnza.kitchenpantry.domain.exception.InsufficientStockException;

public class Product {
    private final ProductName name;
    private final Category category;
    private Quantity quantity;

    public Product(String name, Category category) {
        this.name = new ProductName(name);
        this.category = category;
        this.quantity = Quantity.zero();
    }

    public String getName() {
        return name.value();
    }

    public ProductName getProductName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public int getQuantity() {
        return quantity.value();
    }

    public void increaseQuantity(int amount) {
        this.quantity = this.quantity.add(amount);
    }

    public void decreaseQuantity(int amount) {
        if (amount > quantity.value()) {
            throw new InsufficientStockException(name.value(), amount, this.quantity.value());
        }
        this.quantity = this.quantity.subtract(amount);
    }

    public boolean hasName(String candidate) {
        return this.name.equals(new ProductName(candidate));
    }
}
