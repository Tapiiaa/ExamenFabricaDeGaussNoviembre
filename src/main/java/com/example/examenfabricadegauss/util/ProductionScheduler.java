package com.example.examenfabricadegauss.util;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.PriorityBlockingQueue;

@Component
public class ProductionScheduler {
    private static final Logger logger = LoggerFactory.getLogger(ProductionScheduler.class);

    private final PriorityBlockingQueue<ScheduledTask> queue = new PriorityBlockingQueue<>();
    private final ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    public ProductionScheduler(ThreadPoolTaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public void scheduleTask(ScheduledTask task) {
        queue.put(task);
        logger.info("Tarea programada: {}", task.getTaskDetails());
    }

    public ScheduledTask getNextTask() {
        return queue.poll();
    }

    @PostConstruct
    private void startTaskProccesing() {
        taskExecutor.execute(() -> {
            while (true) {
                try {
                    ScheduledTask task = queue.take();
                    taskExecutor.execute(() -> processTask(task));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    logger.error("Error al procesar la tarea", e);
                }
            }
        });
    }

    private void processTask(ScheduledTask task){
        int attempt = 0;
        boolean success = false;

        while (attempt < 3 && !success) {
            try {
                logger.info("Ejecutando tarea: {}", task.getTaskDetails());
                task.execute();
                success = true;
                logger.info("Tarea completada: {}", task.getTaskDetails());
            } catch (Exception e) {
                attempt ++;
                logger.error("Error al procesar la tarea: {}", task.getTaskDetails(), e);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException interruptedException) {
                    Thread.currentThread().interrupt();
                    logger.error("Error al dormir el hilo", interruptedException);
                }
            }
        }
        if (!success) {
            logger.error("Fallo al ejecutar la tarea despues de 3 intententos: {}", task.getTaskDetails());
        }
    }
}
