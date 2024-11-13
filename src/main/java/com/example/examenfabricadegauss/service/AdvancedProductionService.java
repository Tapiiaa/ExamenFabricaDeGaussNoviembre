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
        scheduler.scheduleTask(new ScheduledTask(componentType, priority));
    }
}

