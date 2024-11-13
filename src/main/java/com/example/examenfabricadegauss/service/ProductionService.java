package com.example.examenfabricadegauss.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.AmqpException;
import com.example.examenfabricadegauss.config.RabbitConfig;

@Service
public class ProductionService {
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public ProductionService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void produceComponent(String type) {
        try {
            System.out.println("Produciendo componente del tipo: " + type);
            rabbitTemplate.convertAndSend(RabbitConfig.PRODUCTION_QUEUE_NAME, type);
        } catch (AmqpException e) {
            System.err.println("Error en la producción de componente: " + e.getMessage());
            // Implementación de una estrategia de reintento o de manejo de errores
        }
    }
}
