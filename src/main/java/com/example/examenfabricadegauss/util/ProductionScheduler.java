package com.example.examenfabricadegauss.util;

import com.example.examenfabricadegauss.service.WorkStationService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

@Component
public class ProductionScheduler {
    private static final Logger logger = LoggerFactory.getLogger(ProductionScheduler.class);

    private final PriorityBlockingQueue<ScheduledTask> queue = new PriorityBlockingQueue<>();
    private final ThreadPoolTaskExecutor taskExecutor;
    private final WorkStationService workStation;

    @Autowired
    public ProductionScheduler(@Qualifier("customTaskExecutor") ThreadPoolTaskExecutor taskExecutor, WorkStationService workStation) {
        this.taskExecutor = taskExecutor;
        this.workStation = workStation;
    }

    public void scheduleTask(ScheduledTask task ) {
        queue.put(task);
        logger.info("Tarea programada: {}", task.getTaskDetails());
    }

    public ScheduledTask getNextTask() {
        return queue.poll();
    }

    @PostConstruct
    private void monitorThreadPool() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(5000);
                    ThreadPoolExecutor executor = taskExecutor.getThreadPoolExecutor();
                    logger.info("Hilos activos: {} | Tamaño del pool: {} | Tareas completadas: {} | Tareas en cola: {}",
                            executor.getActiveCount(),
                            executor.getPoolSize(),
                            executor.getCompletedTaskCount(),
                            executor.getQueue().size());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }

    @PostConstruct
    private void startTaskProcessing() {
        taskExecutor.execute(() -> {
            while (!Thread.currentThread().isInterrupted()) {  // Verifica si el hilo ha sido interrumpido
                try {
                    ScheduledTask task = queue.take();  // Espera a que haya una tarea disponible en la cola
                    taskExecutor.execute(() -> processTask(task, workStation));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();  // Restaurar el estado de interrupción del hilo
                    logger.error("El hilo fue interrumpido mientras esperaba la siguiente tarea. Finalizando la ejecución.", e);
                    break;  // Salir del bucle si el hilo es interrumpido
                }
            }
        });
    }



    private void processTask(ScheduledTask task, WorkStationService station) {
        int attempt = 0;
        boolean success = false;

        while (attempt < 3 && !success) {
            try {
                logger.info("Ejecutando tarea: {} en la estacion: {}", task.getTaskDetails(), station.getClass().getSimpleName());
                station.produceComponent(task.getTaskDetails())
                                .thenAccept(component -> logger.info("Componente producido: {}", component));
                success = true;
                logger.info("Tarea completada: {}", task.getTaskDetails());
            } catch (Exception e) {
                attempt++;
                logger.error("Error al procesar la tarea: {}", task.getTaskDetails(), e);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException interruptedException) {
                    Thread.currentThread().interrupt();  // Restaurar el estado de interrupción
                    logger.error("Error al dormir el hilo durante el intento de reintento", interruptedException);
                    break;  // Salir del bucle de reintento si el hilo es interrumpido
                }
            }
        }

        if (!success) {
            logger.error("Fallo al ejecutar la tarea después de 3 intentos: {}", task.getTaskDetails());
        }
    }
}
