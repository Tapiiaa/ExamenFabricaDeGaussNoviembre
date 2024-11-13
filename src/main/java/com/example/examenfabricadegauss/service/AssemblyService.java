package com.example.examenfabricadegauss.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.example.examenfabricadegauss.config.RabbitConfig;
import com.example.examenfabricadegauss.model.AssemblyStatus;
import com.example.examenfabricadegauss.model.Component;

@Service
public class AssemblyService {

    private static final Logger logger = LoggerFactory.getLogger(AssemblyService.class);
    private final AssemblyStatus assemblyStatus;
    private final int requiredComponents;

    public AssemblyService() {
        this.assemblyStatus = new AssemblyStatus("assembly-1");
        this.requiredComponents = 5;
    }

    @RabbitListener(queues = RabbitConfig.ASSEMBLY_QUEUE_NAME)
    public void receiveComponent(Component component) {
        assemblyStatus.addComponent(component);
        if (assemblyStatus.isComplete(requiredComponents)) {
            assemblyStatus.setStatus("Completado");
            logger.info("Ensamblaje completo: {}", assemblyStatus);
            resetAssemblyStatus();
        }
        logger.info("Ensamblaje de componente: {}", assemblyStatus); 
    }

    private void resetAssemblyStatus() {
        logger.info("Reiniciando el estado del ensamblaje para una nueva produccion");
        this.assemblyStatus.clearComponents();
        this.assemblyStatus.setStatus("En proceso");
    }
}
