package com.example.examenfabricadegauss.util;

import org.springframework.stereotype.Component;

import java.util.concurrent.PriorityBlockingQueue;

@Component
public class ProductionScheduler {
    private final PriorityBlockingQueue<ScheduledTask> queue = new PriorityBlockingQueue<>();

    public void scheduleTask(ScheduledTask task) {
        queue.put(task);
        System.out.println("Tarea programada: " + task.getTaskDetails());
    }

    public ScheduledTask getNextTask() {
        return queue.poll();
    }
}
