package org.jossegonnza.kitchenpantry.domain;

public class ProductName {
    private final String value;

    public ProductName(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        this.value = value.trim();
    }

    public String value() {
        return value;
    }
}
