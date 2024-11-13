package com.example.examenfabricadegauss.service;

import com.example.examenfabricadegauss.util.ProductionScheduler;
import com.example.examenfabricadegauss.util.ScheduledTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdvancedProductionService {
    private final ProductionScheduler scheduler;
    private static final Logger logger = LoggerFactory.getLogger(AdvancedProductionService.class);

    @Autowired
    public AdvancedProductionService(ProductionScheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void handleProductionRequest(String componentType, int priority) {
        try {
            validateRequest(componentType, priority);
            scheduler.scheduleTask(new ScheduledTask(componentType, priority));
        } catch (Exception e) {
            logger.error("Error al programar la producción del componente: {}", componentType, e);
            handleRetry(new ScheduledTask(componentType, priority), 0);
        }
    }

    private void handleRetry(ScheduledTask task, int retryCount) {
        if (retryCount < 3) {
            try {
                Thread.sleep(3000);
                logger.info("Reintentando producción para el componente: {} (Intento {})", task.getComponentType(), retryCount + 1);
                scheduler.scheduleTask(task);
            } catch (Exception e) {
                logger.error("Error al reintentar producción para el componente: {}, Error: {}", task.getComponentType(), e.getMessage());
                handleRetry(task, retryCount + 1);
            }
        } else {
            logger.error("Se ha excedido el número máximo de reintentos para el componente: {}", task.getComponentType());
        }
    }

    private void validateRequest(String componentType, int priority){
        if (componentType == null || componentType.isEmpty()) {
            throw new IllegalArgumentException("El tipo de componente no puede ser nulo o vacío");
        } else if (priority < 1 || priority > 10) {
            throw new IllegalArgumentException("La prioridad debe estar entre 1 y 10");
        }
    }
}

