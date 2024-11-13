package com.example.examenfabricadegauss.service;

import com.example.examenfabricadegauss.util.ProductionScheduler;
import com.example.examenfabricadegauss.util.ScheduledTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdvancedProductionService {
    private final ProductionScheduler scheduler;

    @Autowired
    public AdvancedProductionService(ProductionScheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void handleProductionRequest(String componentType, int priority) {
        if (componentType == null || componentType.isEmpty()) {
            throw new IllegalArgumentException("El tipo de componente no puede ser nulo o vacío");
        } else if (priority < 1 || priority > 10) {
            throw new IllegalArgumentException("La prioridad debe estar entre 1 y 10");
        }
        scheduler.scheduleTask(new ScheduledTask(componentType, priority));
    }
}

