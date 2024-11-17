package com.example.examenfabricadegauss.util;

import com.example.examenfabricadegauss.config.RabbitConfig;
import com.example.examenfabricadegauss.service.WorkStationService;
import jakarta.annotation.PostConstruct;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class ProductionScheduler {
    private static final Logger logger = LoggerFactory.getLogger(ProductionScheduler.class);

    private final PriorityBlockingQueue<ScheduledTask> queue = new PriorityBlockingQueue<>();
    private final ThreadPoolTaskExecutor taskExecutor;
    private final WorkStationService workStation;
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public ProductionScheduler(@Qualifier("customTaskExecutor") ThreadPoolTaskExecutor taskExecutor, WorkStationService workStation, RabbitTemplate rabbitTemplate) {
        this.taskExecutor = taskExecutor;
        this.workStation = workStation;
        this.rabbitTemplate = rabbitTemplate;
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
                    Thread.sleep(2000);
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
        AtomicBoolean success = new AtomicBoolean(false);

        while (attempt < 3 && !success.get()) {
            try {
                logger.info("Ejecutando tarea: {} en la estación: {}", task.getTaskDetails(), station.getClass().getSimpleName());

                station.produceComponent(task.getTaskDetails())
                        .thenAccept(component -> {
                            logger.info("Componente producido: {}", component);

                            // Enviar mensaje a RabbitMQ
                            try {
                                rabbitTemplate.convertAndSend(RabbitConfig.PRODUCTION_QUEUE_NAME, component);
                                logger.info("Mensaje enviado a RabbitMQ: {}", component);
                                success.set(true); // Marca la tarea como exitosa si RabbitMQ no falla
                            } catch (Exception e) {
                                logger.error("Error al enviar el componente a RabbitMQ: {}", component, e);
                            }
                        })
                        .exceptionally(e -> {
                            logger.error("Error al producir el componente: {}", task.getTaskDetails(), e);
                            return null;
                        });

                // Pausa breve para permitir el procesamiento del CompletableFuture
                Thread.sleep(2000);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restaurar el estado de interrupción
                logger.error("El hilo fue interrumpido durante la ejecución de la tarea: {}", task.getTaskDetails(), e);
                break; // Salir del bucle si el hilo fue interrumpido
            }

            attempt++;
            if (!success.get()) {
                logger.info("Reintentando la tarea: {} (Intento {})", task.getTaskDetails(), attempt);
                try {
                    Thread.sleep(3000); // Esperar antes de reintentar
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    logger.error("El hilo fue interrumpido durante el reintento: {}", task.getTaskDetails());
                    break;
                }
            }
        }

        if (!success.get()) {
            logger.error("Fallo al ejecutar la tarea después de 3 intentos: {}", task.getTaskDetails());
        }
    }
}
