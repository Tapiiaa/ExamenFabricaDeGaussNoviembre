package com.example.examenfabricadegauss.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.examenfabricadegauss.config.RabbitConfig;


@RestController
@RequestMapping("/test")
public class SimulationController {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public SimulationController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping("/send")
    public String send(@RequestParam("msg") String message) {
        rabbitTemplate.convertAndSend(RabbitConfig.queueName, message);
        return "Message sent to the queue: " + message;
    }
}
