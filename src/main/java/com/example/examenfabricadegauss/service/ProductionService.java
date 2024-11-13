package com.example.examenfabricadegauss.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.AmqpException;
import com.example.examenfabricadegauss.config.RabbitConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ProductionService {
    private static final Logger logger = LoggerFactory.getLogger(ProductionService.class);
    private final RabbitTemplate rabbitTemplate;
    private final ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    public ProductionService(RabbitTemplate rabbitTemplate, ThreadPoolTaskExecutor taskExecutor) {
        this.rabbitTemplate = rabbitTemplate;
        this.taskExecutor = taskExecutor;
    }

    public void produceComponent(String type) {
        taskExecutor.execute(() -> {
            try {
                logger.info("Produciendo componente del tipo: {}", type);
                rabbitTemplate.convertAndSend(RabbitConfig.PRODUCTION_QUEUE_NAME, type);
            } catch (AmqpException e) {
                logger.error("Error en la producción del componente: {}", e.getMessage());
                handleRetry(type, 0);
            }
        });
    }

    private void handleRetry(String type, int retryCount) {
        if (retryCount < 3) {
            try {
                Thread.sleep(5000); // Espera 5 segundos antes de reintentar
                logger.info("Reintentando producir componente del tipo: {}. Intento {}", type, retryCount + 1);
                rabbitTemplate.convertAndSend(RabbitConfig.PRODUCTION_QUEUE_NAME, type);
            } catch (InterruptedException | AmqpException e) {
                logger.error("Reintento fallido para el componente del tipo: {}, Error: {}", type, e.getMessage());
                handleRetry(type, retryCount + 1);
            }
        } else {
            logger.error("Fallo en producir el componente del tipo: {} después de {} intentos", type, retryCount);
        }
    }
}
