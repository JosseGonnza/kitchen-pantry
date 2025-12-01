package org.jossegonnza.kitchenpantry;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AddProductTest {
    @Test
    void shouldAddProductNewProductToEmptyPantry() {
        Pantry pantry = new Pantry();

        pantry.addProduct("Rice");
        List<Product> products = pantry.getProducts();

        assertEquals(1, products.size());
        assertEquals("Rice", products.get(0).getName());
    }

    @Test
    void shouldThrowWhenAddingProductWithExistingName() {
        Pantry pantry = new Pantry();
        pantry.addProduct("Rice", Category.GRAINS);

        assertThrows(DuplicateProductException.class,
                () -> pantry.addProduct("Rice", Category.GRAINS));
    }

    @Test
    void shouldNotAllowSameNameWithDifferentCategory() {
        Pantry pantry = new Pantry();
        pantry.addProduct("Rice", Category.GRAINS);

        assertThrows(DuplicateProductException.class,
                () -> pantry.addProduct("Rice", Category.CLEANING));
    }
}
