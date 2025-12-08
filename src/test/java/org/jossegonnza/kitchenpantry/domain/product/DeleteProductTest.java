package org.jossegonnza.kitchenpantry.domain.product;

import org.jossegonnza.kitchenpantry.domain.Category;
import org.jossegonnza.kitchenpantry.domain.Pantry;
import org.jossegonnza.kitchenpantry.domain.Product;
import org.jossegonnza.kitchenpantry.domain.exception.ProductNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeleteProductTest {
    @Test
    void shouldDeleteProductProductWhenItExistsInPantry() {
        Pantry pantry = new Pantry();
        pantry.addProduct("Rice");

        pantry.deleteProduct("Rice");
        List<Product> products = pantry.getProducts();

        assertEquals(0, products.size());
    }

    @Test
    void shouldThrowWhenDeletingNonExistingProduct() {
        Pantry pantry = new Pantry();
        pantry.addProduct("Rice", Category.GRAINS);

        assertThrows(ProductNotFoundException.class, () -> pantry.deleteProduct("Pasta"));
    }
}
