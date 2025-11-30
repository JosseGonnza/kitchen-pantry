package org.jossegonnza.pantry;

public class Product {
    private final String name;
    private final Category category;

    public Product(String name, Category category) {
        this.name = name;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }
}
