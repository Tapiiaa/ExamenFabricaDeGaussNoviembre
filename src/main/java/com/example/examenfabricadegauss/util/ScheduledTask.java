package com.example.examenfabricadegauss.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduledTask implements Comparable<ScheduledTask> {
    private String componentType;
    private int priority;

    public ScheduledTask(String componentType, int priority) {
        this.componentType = componentType;
        this.priority = priority;
    }

    public String getTaskDetails() {
        return "Componente: " + componentType + ", Prioridad: " + priority;
    }

    @Override
    public int compareTo(ScheduledTask o) {
        return Integer.compare(this.priority, o.priority);
    }
}
