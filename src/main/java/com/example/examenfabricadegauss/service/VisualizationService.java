package com.example.examenfabricadegauss.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class VisualizationService {

    private final SimpMessagingTemplate template;

    @Autowired
    public VisualizationService(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void sendUpdate(Object updateInfo) {
        template.convertAndSend("/topic/productionUpdate", updateInfo);
    }
}
