package com.example.examenfabricadegauss.util;

import com.example.examenfabricadegauss.service.ProductionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScheduledTask implements Comparable<ScheduledTask>, Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTask.class);

    private final Runnable task; // Asegúrate de que este sea de tipo Runnable (una interfaz funcional)
    private final String description;
    private final int priority;
    private final long scheduledTime;
    private final Object associatedService;

    public ScheduledTask(Runnable task, String description, int priority, Object associatedService) {
        this(task, description, priority, associatedService, System.currentTimeMillis());
    }

    public ScheduledTask(Runnable task, String description, int priority, Object associatedService, long scheduledTime) {
        this.task = task;
        this.description = description;
        this.priority = priority;
        this.scheduledTime = scheduledTime;
        this.associatedService = associatedService;
    }

    @Override
    public void run() {
        // Ejecuta la tarea asociada
        if (task != null) {
            logger.info("Ejecutando tarea: {}", description);
            try {
                task.run();
                logger.info("Tarea completada con éxito: {}", description);
            } catch (Exception e) {
                logger.error("Error al ejecutar la tarea: {}", description, e);
            }
        }
    }

    @Override
    public int compareTo(ScheduledTask other) {
        // Primero, comparo por prioridad
        int priorityComparison = Integer.compare(this.priority, other.priority);
        if (priorityComparison != 0) {
            return priorityComparison;
        }

        // Si la prioridad es la misma, comparo por tiempo de programación
        return Long.compare(this.scheduledTime, other.scheduledTime);
    }

    // Métodos getter si son necesarios
    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }


    public long getScheduledTime() {
        return scheduledTime;
    }

    // Nuevo método: getTaskDetails
    public String getTaskDetails() {
        return String.format("Descripción: %s, Prioridad: %d, Programada para: %d", description, priority, scheduledTime);
    }

    // Nuevo método: execute
    public void execute() {
        // Ejecuta la tarea
        logger.info("Ejecutando la lógica de la tarea desde el método execute: {}", description);
        run(); // Llama al método run() que contiene la lógica de la tarea
    }
}
