package com.example.examenfabricadegauss.util;

import com.example.examenfabricadegauss.model.Component;
import com.example.examenfabricadegauss.service.ProductionService;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Setter
public class ScheduledTask implements Comparable<ScheduledTask> {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTask.class);
    private String componentType;
    private int priority;
    private final ProductionService productionService;

    public ScheduledTask(String componentType, int priority, ProductionService productionService) {
        this.componentType = componentType;
        this.priority = priority;
        this.productionService = productionService;
    }

    public String getTaskDetails() {
        return "Componente: " + componentType + ", Prioridad: " + priority;
    }

    @Override
    public int compareTo(ScheduledTask o) {
        return Integer.compare(this.priority, o.priority);
    }

    public void execute() {
        try {
            productionService.produceComponent(componentType);
            Thread.sleep(1000);
            logger.info("Componente de tipo: {} producido y enviado a la cola", componentType);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Tarea interrumpida", e);
        }
    }
}
