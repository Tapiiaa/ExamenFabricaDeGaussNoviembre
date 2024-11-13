package com.example.examenfabricadegauss.service;

import com.example.examenfabricadegauss.config.RabbitConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class AssemblyService {

    @RabbitListener(queues = RabbitConfig.ASSEMBLY_QUEUE_NAME)
    public void receiveComponent(String componentType) {
        System.out.println("Ensamblaje de componente: " + componentType);
        // Añadir aquí la lógica de ensamblaje
    }
}
