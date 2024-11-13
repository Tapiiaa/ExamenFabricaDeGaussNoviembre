package com.example.examenfabricadegauss.controller;

import com.example.examenfabricadegauss.service.ProductionService;
import com.example.examenfabricadegauss.service.AssemblyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/simulate")
public class SimulationController {

    private final ProductionService productionService;
    private final AssemblyService assemblyService;

    @Autowired
    public SimulationController(ProductionService productionService, AssemblyService assemblyService) {
        this.productionService = productionService;
        this.assemblyService = assemblyService;
    }

    @GetMapping("/produce")
    public String simulateProduction() {
        productionService.receiveMessage("Simular mensaje de producción");
        return "Simulación de producción iniciada";
    }

    @GetMapping("/assemble")
    public String simulateAssembly() {
        assemblyService.assembleComponents();
        return "Simulación de ensamblaje iniciada";
    }
}
