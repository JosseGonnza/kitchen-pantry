package org.jossegonnza.kitchenpantry;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetProductsByCategoryTest {
    @Test
    void shouldStoreCategoryWhenAddingProduct() {
        Pantry pantry = new Pantry();
        pantry.addProduct("Rice", Category.GRAINS);

        Product rice = pantry.findByName("Rice")
                .orElseThrow(() -> new AssertionError("Product 'Rice' should exist"));

        assertEquals(Category.GRAINS, rice.getCategory());
    }

    @Test
    void shouldReturnOnlyProductsFromRequestedCategory() {
        Pantry pantry = new Pantry();
        pantry.addProduct("Rice", Category.GRAINS);
        pantry.addProduct("Pasta", Category.GRAINS);
        pantry.addProduct("Chicken", Category.MEAT);
        pantry.addProduct("Soap", Category.CLEANING);

        List<Product> products = pantry.getProductsByCategory(Category.GRAINS);

        assertEquals(2, products.size());
        assertEquals("Rice", products.get(0).getName());
        assertEquals("Pasta", products.get(1).getName());
    }

    @Test
    void shouldReturnEmptyListWhenNoProductsMatchCategory() {
        Pantry pantry = new Pantry();
        pantry.addProduct("Rice", Category.GRAINS);

        List<Product> cleaningProducts = pantry.getProductsByCategory(Category.CLEANING);

        assertTrue(cleaningProducts.isEmpty());
    }
}
