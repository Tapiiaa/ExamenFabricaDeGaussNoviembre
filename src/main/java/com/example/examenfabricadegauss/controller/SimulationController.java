package com.example.examenfabricadegauss.controller;

import com.example.examenfabricadegauss.service.ProductionService;
import com.example.examenfabricadegauss.util.ProductionScheduler;
import com.example.examenfabricadegauss.util.ScheduledTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/simulate")
public class SimulationController {
    private final ProductionScheduler scheduler;

    Logger logger = LoggerFactory.getLogger(SimulationController.class);
    @Autowired
    public SimulationController(ProductionScheduler scheduler) {
        this.scheduler = scheduler;
    }

    @GetMapping("/produce")
    public String simulateProduction(@RequestParam String type, @RequestParam(defaultValue = "10") int count) {
        for (int i = 0; i < count; i++) {
            // Crear la tarea como un Runnable con lógica de producción
            Runnable taskLogic = () -> {
                try {
                    logger.info("Produciendo componente del tipo: {}", type);
                    Thread.sleep(2000);
                    logger.info("Componente producido: {}", type);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    logger.error("Error al producir el componente: " + e.getMessage());
                }
            };

            ScheduledTask task = new ScheduledTask(
                    taskLogic,
                    "Componente " + type + i,
                    i,
                    System.currentTimeMillis()
            );

            // Programar la tarea en el scheduler
            scheduler.scheduleTask(task);
        }
        return "Producción simulada de " + count + " para componente tipo: " + type;
    }
}
