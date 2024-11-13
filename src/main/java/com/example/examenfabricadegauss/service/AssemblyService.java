package com.example.examenfabricadegauss.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.example.examenfabricadegauss.config.RabbitConfig;

@Service
public class AssemblyService {

    private static final Logger logger = LoggerFactory.getLogger(AssemblyService.class);

    @RabbitListener(queues = RabbitConfig.ASSEMBLY_QUEUE_NAME)
    public void receiveComponent(String componentType) {
        logger.info("Ensamblaje de componente: {}", componentType); 
        // Añadir aquí la lógica de ensamblaje
    }
}
