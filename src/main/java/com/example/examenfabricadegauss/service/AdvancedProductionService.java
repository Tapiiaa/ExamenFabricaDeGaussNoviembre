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
    private final ProductionService productionService;

    @Autowired
    public AdvancedProductionService(ProductionScheduler scheduler, ProductionService productionService) {
        this.scheduler = scheduler;
        this.productionService = productionService;
    }

    public void handleProductionRequest(String componentType, int priority) {
        try {
            validateRequest(componentType, priority);
            ScheduledTask task = new ScheduledTask(
                    () -> productionService.produceComponent(componentType), // Runnable con lógica de producción
                    "Producción de componente: " + componentType, // Descripción de la tarea
                    priority, // Prioridad de la tarea
                    productionService, // Servicio de producción
                    System.currentTimeMillis() // Tiempo actual para programar la tarea
            );
            scheduler.scheduleTask(task);
        } catch (Exception e) {
            logger.error("Error al programar la producción del componente: {}", componentType, e);
            handleRetry(componentType, priority, 0);
        }
    }

    private void handleRetry(String componentType, int priority, int retryCount) {
        if (retryCount < 3) {
            try {
                Thread.sleep(3000);
                logger.info("Reintentando producción para el componente: {} (Intento {})", componentType, retryCount + 1);
                ScheduledTask task = new ScheduledTask(
                        () -> productionService.produceComponent(componentType),
                        "Reintento de producción de componente: " + componentType,
                        priority,
                        productionService,
                        System.currentTimeMillis()
                );
                scheduler.scheduleTask(task);
            } catch (Exception e) {
                logger.error("Error al reintentar producción para el componente: {}, Error: {}", componentType, e.getMessage());
                handleRetry(componentType, priority, retryCount + 1);
            }
        } else {
            logger.error("Se ha excedido el número máximo de reintentos para el componente: {}", componentType);
        }
    }

    private void validateRequest(String componentType, int priority) {
        if (componentType == null || componentType.isEmpty()) {
            throw new IllegalArgumentException("El tipo de componente no puede ser nulo o vacío");
        } else if (priority < 1 || priority > 10) {
            throw new IllegalArgumentException("La prioridad debe estar entre 1 y 10");
        }
    }
}
