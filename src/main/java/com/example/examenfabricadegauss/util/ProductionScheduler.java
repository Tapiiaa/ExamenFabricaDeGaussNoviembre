package com.example.examenfabricadegauss.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.PriorityBlockingQueue;

@Component
public class ProductionScheduler {
    private static final Logger logger = LoggerFactory.getLogger(ProductionScheduler.class);

    private final PriorityBlockingQueue<ScheduledTask> queue = new PriorityBlockingQueue<>();
    private final ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    public ProductionScheduler(ThreadPoolTaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public void scheduleTask(ScheduledTask task) {
        queue.put(task);
        System.out.println("Tarea programada: " + task.getTaskDetails());
    }

    public ScheduledTask getNextTask() {
        return queue.poll();
    }
}
