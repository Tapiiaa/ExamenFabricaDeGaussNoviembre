package com.example.examenfabricadegauss.service;

import org.springframework.stereotype.Service;

@Service
public class ProductionService {

    public void receiveMessage(String message) {
        System.out.println("Recibido <" + message + ">");
        // Procesar el mensaje recibido, transformarlo en componentes, etc.
    }

    //Patrik, completar si ves necesario:
    private void processMessage(String message) {
        System.out.println("Procesando mensaje: " + message); //Esto como me comentaste, procesamos la señal como msj.
    }
}
