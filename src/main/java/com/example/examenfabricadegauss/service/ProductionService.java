package com.example.examenfabricadegauss.service;

import com.example.examenfabricadegauss.model.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.AmqpException;
import com.example.examenfabricadegauss.config.RabbitConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Service
public class ProductionService implements WorkStationService {
    private static final Logger logger = LoggerFactory.getLogger(ProductionService.class);
    private final RabbitTemplate rabbitTemplate;
    private final ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    public ProductionService(RabbitTemplate rabbitTemplate, ThreadPoolTaskExecutor taskExecutor) {
        this.rabbitTemplate = rabbitTemplate;
        this.taskExecutor = taskExecutor;
    }

    @Override
    public CompletableFuture<Component> produceComponent(String type) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("Produciendo componente del tipo: {}", type);
                Thread.sleep(2000);

                Component component = new Component(type);
                component.setId("COMP-" + LocalDateTime.now());
                component.setType(type);
                component.setTimestamp(LocalDateTime.now());


                rabbitTemplate.convertAndSend(RabbitConfig.PRODUCTION_QUEUE_NAME, type);
                logger.info("Componente producido: {}", type);

                return component;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Error al producir el componente", e);
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
