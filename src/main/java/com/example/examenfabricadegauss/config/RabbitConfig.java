package com.example.examenfabricadegauss.config;

import com.example.examenfabricadegauss.service.AssemblyService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;

@Configuration
public class RabbitConfig {

    public static final String PRODUCTION_QUEUE_NAME = "productionQueue";
    public static final String ASSEMBLY_QUEUE_NAME = "assemblyQueue";

    @Bean
    public Queue productionQueue() {
        return new Queue(PRODUCTION_QUEUE_NAME, true);
    }

    @Bean
    public Queue assemblyQueue() {
        return new Queue(ASSEMBLY_QUEUE_NAME, true);
    }

    @Bean
    public MessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory,
                                                             MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(ASSEMBLY_QUEUE_NAME);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    public MessageListenerAdapter messageListenerAdapter(AssemblyService assemblyService) {
        return new MessageListenerAdapter(assemblyService, "receiveComponent");
    }
}

