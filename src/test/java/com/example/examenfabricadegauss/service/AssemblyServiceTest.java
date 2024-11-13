package com.example.examenfabricadegauss.service;

import com.example.examenfabricadegauss.model.Component;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AssemblyServiceTest {
    AssemblyService assemblyService = new AssemblyService();

    @Test
    void testReceiveComponent() {
        Component component = new Component("component-1", "type-1");
        assemblyService.receiveComponent(component);
        assertEquals("Pending", assemblyService.getAssemblyStatus().getStatus());
        assertEquals(1, assemblyService.getAssemblyStatus().getComponents().size());
    }

   @Test
    void testReceiveComponentNull() {
        assemblyService.receiveComponent(null);
        assertEquals("Pending", assemblyService.getAssemblyStatus().getStatus());
        assertEquals(0, assemblyService.getAssemblyStatus().getComponents().size());
    }


    @Test
    void testResetAssemblyStatus() {
        Component component = new Component("component-1", "type-1");
        assemblyService.receiveComponent(component);
        assemblyService.resetAssemblyStatus();
        assertEquals("En proceso", assemblyService.getAssemblyStatus().getStatus());
        assertEquals(0, assemblyService.getAssemblyStatus().getComponents().size());
    }
}