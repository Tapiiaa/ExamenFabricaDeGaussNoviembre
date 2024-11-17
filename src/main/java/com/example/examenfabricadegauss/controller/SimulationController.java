package com.example.examenfabricadegauss.controller;

import com.example.examenfabricadegauss.service.ProductionService;
import com.example.examenfabricadegauss.util.ProductionScheduler;
import com.example.examenfabricadegauss.util.ScheduledTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/simulate")
public class SimulationController {
    private final ProductionScheduler scheduler;

    @Autowired
    public SimulationController(ProductionScheduler scheduler) {
        this.scheduler = scheduler;
    }

    @GetMapping("/produce")
    public String simulateProduction(@RequestParam String type, @RequestParam int count) {
        for (int i = 0; i < count; i++) {
            // Crear la tarea como un Runnable con lógica de producción
            Runnable taskLogic = () -> {
                System.out.println("Produciendo componente del tipo: " + type);
                try {
                    Thread.sleep(1000); // Simula tiempo de producción
                    System.out.println("Componente producido: " + type);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Error al producir el componente: " + e.getMessage());
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
