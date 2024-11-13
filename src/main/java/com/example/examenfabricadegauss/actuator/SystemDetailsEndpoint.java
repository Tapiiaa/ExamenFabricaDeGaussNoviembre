package com.example.examenfabricadegauss.actuator;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

@Component
@Endpoint(id = "systemDetails")
public class SystemDetailsEndpoint {

    @ReadOperation
    public SystemDetails systemDetails() {
        // Lógica para recopilar detalles del sistema
        return new SystemDetails("OK", Runtime.getRuntime().freeMemory());
    }

    static class SystemDetails {
        private String status;
        private long freeMemory;

        SystemDetails(String status, long freeMemory) {
            this.status = status;
            this.freeMemory = freeMemory;
        }

        // Getters
        public String getStatus() {
            return status;
        }

        public long getFreeMemory() {
            return freeMemory;
        }
    }
}
