package org.jossegonnza.kitchenpantry.application;

import org.jossegonnza.kitchenpantry.domain.Category;
import org.jossegonnza.kitchenpantry.domain.Pantry;
import org.jossegonnza.kitchenpantry.domain.Product;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PantryServiceAddProductTest {
    @Test
    void shouldAddNewProductToPantry() {
        PantryService service = new PantryService(new Pantry());

        service.addProduct("Rice", Category.GRAINS);
        List<Product> products = service.getAllProducts();

        assertEquals(1, products.size());
        assertEquals("Rice", products.getFirst().getName());
        assertEquals(Category.GRAINS, products.getFirst().getCategory());
    }
}
