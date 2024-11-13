package com.example.examenfabricadegauss.service;

import org.springframework.stereotype.Service;

@Service
public class ProductionService {

    public void receiveMessage(String message) {
        System.out.println("Received <" + message + ">");
        // Procesar el mensaje recibido, transformarlo en componentes, etc.

    }
}
