package com.example.examenfabricadegauss.controller;

import com.example.examenfabricadegauss.service.ProductionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/simulate")
public class SimulationController {
    private final ProductionService productionService;

    @Autowired
    public SimulationController(ProductionService productionService) {
        this.productionService = productionService;
    }

    @GetMapping("/produce")
    public String simulateProduction(@RequestParam String type) {
        productionService.produceComponent(type);
        return "Producción simulada para componente tipo: " + type;
    }
}
