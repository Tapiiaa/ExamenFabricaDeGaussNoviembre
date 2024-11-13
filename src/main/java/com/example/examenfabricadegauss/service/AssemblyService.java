package com.example.examenfabricadegauss.service;

import org.springframework.stereotype.Service;

@Service
public class AssemblyService {

    public void assembleMachine() {
        // Lógica para ensamblar la máquina a partir de componentes disponibles
        System.out.println("Assembling machine from available components.");
    }

    public void addComponentToAssemblyQueue(String componentId) {
        System.out.println("Añadiendo componente: " + componentId);
    }

    public void assembleComponents(){
        System.out.println("Ensamblaje de componentes en una máquina");
    }
}
