package org.jossegonnza.kitchenpantry.api.stock;

import org.jossegonnza.kitchenpantry.api.stock.dto.StockSummaryResponse;
import org.jossegonnza.kitchenpantry.application.PantryService;
import org.jossegonnza.kitchenpantry.domain.Category;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
public class StockController {
    private final PantryService pantryService;

    public StockController(PantryService pantryService) {
        this.pantryService = pantryService;
    }

    @GetMapping("/summary")
    public List<StockSummaryResponse> getStockSummary() {
        return pantryService.getStockSummary()
                .stream()
                .map(StockSummaryResponse::from)
                .toList();
    }

    @GetMapping("/low")
    public List<StockSummaryResponse> getLowStock(@RequestParam int threshold,
                                                  @RequestParam(required = false) Category category) {
        if (category == null) {
            return pantryService.getProductsBelowQuantity(threshold)
                    .stream()
                    .map(StockSummaryResponse::from)
                    .toList();
        }
        return pantryService.getProductsBelowQuantity(category, threshold)
                .stream()
                .map(StockSummaryResponse::from)
                .toList();
    }
}
