package com.example.examenfabricadegauss.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.AmqpException;
import org.springframework.beans.factory.annotation.Qualifier;
import com.example.examenfabricadegauss.config.RabbitConfig;

@Service
public class ProductionService {
    private final RabbitTemplate rabbitTemplate;
    private final ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    public ProductionService(RabbitTemplate rabbitTemplate,
                             @Qualifier("taskExecutor") ThreadPoolTaskExecutor taskExecutor) {
        this.rabbitTemplate = rabbitTemplate;
        this.taskExecutor = taskExecutor;
    }

    public void produceComponent(String type) {
        taskExecutor.execute(() -> {
            try {
                String threadName = Thread.currentThread().getName();
                System.out.println(threadName + " - Produciendo componente del tipo: " + type);
                rabbitTemplate.convertAndSend(RabbitConfig.PRODUCTION_QUEUE_NAME, type);
            } catch (AmqpException e) {
                System.err.println("Error en la producción de componente: " + e.getMessage());
                // Implementación de una estrategia de reintento o de manejo de errores
            }
        });
    }
}
