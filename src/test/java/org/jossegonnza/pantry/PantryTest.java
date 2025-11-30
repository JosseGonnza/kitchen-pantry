package org.jossegonnza.pantry;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PantryTest {
    @Test
    void shouldAddProductNewProductToEmptyPantry() {
        Pantry pantry = new Pantry();

        pantry.addProduct("Rice");
        List<Product> products = pantry.getProducts();

        assertEquals(1, products.size());
        assertEquals("Rice", products.get(0).getName());
    }

    @Test
    void shouldNotAddProductADuplicateProduct() {
        Pantry pantry = new Pantry();
        pantry.addProduct("Rice");

        pantry.addProduct("Rice");
        List<Product> products = pantry.getProducts();

        assertEquals(1, products.size());
    }

    @Test
    void shouldDeleteProductProductWhenItExistsInPantry() {
        Pantry pantry = new Pantry();
        pantry.addProduct("Rice");

        pantry.deleteProduct("Rice");
        List<Product> products = pantry.getProducts();

        assertEquals(0, products.size());
    }

    @Test
    void shouldFindProductByNameWhenItExists() {
        Pantry pantry = new Pantry();
        pantry.addProduct("Rice");
        pantry.addProduct("Pasta");

        Optional<Product> product = pantry.findByName("Rice");

        assertTrue(product.isPresent());
        assertEquals("Rice", product.get().getName());
    }

    @Test
    void shouldReturnEmptyWhenProductDoesNotExist() {
        Pantry pantry = new Pantry();
        pantry.addProduct("Rice");

        Optional<Product> product = pantry.findByName("Pasta");

        assertTrue(product.isEmpty());
    }

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
}