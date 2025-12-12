package org.jossegonnza.kitchenpantry.api.batch;

import jakarta.validation.Valid;
import org.jossegonnza.kitchenpantry.api.batch.dto.BatchResponse;
import org.jossegonnza.kitchenpantry.api.batch.dto.CreateBatchRequest;
import org.jossegonnza.kitchenpantry.application.PantryService;
import org.jossegonnza.kitchenpantry.domain.Batch;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product/{productName}/batches")
public class BatchController {
    private final PantryService pantryService;

    public BatchController(PantryService pantryService) {
        this.pantryService = pantryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addBatch(@PathVariable String productName, @Valid @RequestBody CreateBatchRequest request) {
        pantryService.addBatch(productName, request.amount(), request.expiryDate());
    }

    @GetMapping
    public List<BatchResponse> getBatches(@PathVariable String productName) {
        List<Batch> batches = pantryService.getBatches(productName);
        return batches.stream()
                .map(batch -> new BatchResponse(
                        batch.productName().value(),
                        batch.quantity().value(),
                        batch.expiryDate()
                ))
                .toList();
    }
}
