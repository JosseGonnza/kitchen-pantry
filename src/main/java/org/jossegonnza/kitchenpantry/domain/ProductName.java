package org.jossegonnza.kitchenpantry.domain;

public class ProductName {
    private final String name;

    public ProductName(String name) {
        this.name = name;
    }

    public String value() {
        return name;
    }
}
