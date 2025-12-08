package org.jossegonnza.kitchenpantry.domain.productName;

import org.jossegonnza.kitchenpantry.domain.ProductName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProductNameTest {
    @Test
    void shouldCreateProductNameWithNonBlankValue() {
        ProductName name = new ProductName("Rice");

        assertEquals("Rice", name.value());
    }

    @Test
    void shouldTrimValue() {
        ProductName name = new ProductName("   Rice   ");

        assertEquals("Rice", name.value());
    }

    @Test
    void shouldNotAllowNullOrBlankNames() {
        assertThrows(IllegalArgumentException.class,
                () -> new ProductName(null));
        assertThrows(IllegalArgumentException.class,
                () -> new ProductName(" "));
    }
}
