package org.jossegonnza.kitchenpantry.api.product;

import jakarta.validation.Valid;
import org.jossegonnza.kitchenpantry.api.product.dto.ConsumeProductRequest;
import org.jossegonnza.kitchenpantry.api.product.dto.CreateProductRequest;
import org.jossegonnza.kitchenpantry.api.product.dto.ProductResponse;
import org.jossegonnza.kitchenpantry.application.PantryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//http://localhost:8080/swagger-ui/index.html
@RestController
@RequestMapping("api/products")
public class ProductController {
    private final PantryService pantryService;

    public ProductController(PantryService pantryService) {
        this.pantryService = pantryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody CreateProductRequest request){
        pantryService.addProduct(request.name(), request.category());
    }

    @GetMapping
    public List<ProductResponse> getAll() {
        return pantryService.getProducts().stream()
                .map(ProductResponse::from)
                .toList();
    }

    @PostMapping("/{productName}/consume")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void consumeProduct(@PathVariable String productName, @Valid @RequestBody ConsumeProductRequest request) {
        pantryService.consumeProduct(productName, request.amount());
    }
}
