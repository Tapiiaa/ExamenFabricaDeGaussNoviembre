package com.example.examenfabricadegauss.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class AssemblyStatusTest {

    @Test
    void testAssemblyStatus() {
        AssemblyStatus assemblyStatus = new AssemblyStatus("test-assembly");
        assertEquals("Pending", assemblyStatus.getStatus());

        Component component1 = new Component("123", "TypeA");
        assemblyStatus.addComponent(component1);

        assertEquals(1, assemblyStatus.getComponents().size());
        assertFalse(assemblyStatus.isComplete(5));

        // Agrega más componentes para probar si el ensamblaje se completa
        for (int i = 2; i <= 5; i++) {
            assemblyStatus.addComponent(new Component(String.valueOf(i), "TypeB"));
        }
        
        assertTrue(assemblyStatus.isComplete(5));
        assemblyStatus.setStatus("Completed");
        assertEquals("Completed", assemblyStatus.getStatus());
    }
}
