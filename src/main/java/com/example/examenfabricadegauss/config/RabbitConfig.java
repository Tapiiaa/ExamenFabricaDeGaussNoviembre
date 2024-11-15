package com.example.examenfabricadegauss.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.examenfabricadegauss.service.AssemblyService;

@Configuration
public class RabbitConfig {

    // Constantes para las colas, exchange y routing keys, asignadas a través de @Value
    public static final String PRODUCTION_QUEUE_NAME = "productionQueue";
    public static final String ASSEMBLY_QUEUE_NAME = "assemblyQueue";
    public static final String EXCHANGE_NAME = "ExamenFabricaDeGauss2";
    public static final String PRODUCTION_ROUTING_KEY = "production.routing.key";
    public static final String ASSEMBLY_ROUTING_KEY = "assembly.routing.key";

    @Value("${spring.rabbitmq.queue.production:productionQueue}")
    private String productionQueueName;

    @Value("${rabbitmq.queue.assembly:assemblyQueue}")
    private String assemblyQueueName;

    @Value("${spring.application.name:ExamenFabricaDeGauss2}")
    private String exchangeName;

    @Value("${rabbitmq.routing.production:production.routing.key}")
    private String productionRoutingKey;

    @Value("${rabbitmq.routing.assembly:assembly.routing.key}")
    private String assemblyRoutingKey;

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchangeName);
    }

    @Bean
    public Queue productionQueue() {
        return new Queue(productionQueueName, true); // true para cola persistente
    }

    @Bean
    public Queue assemblyQueue() {
        return new Queue(assemblyQueueName, true);
    }

    @Bean
    public Binding productionBinding(TopicExchange exchange, Queue productionQueue) {
        return BindingBuilder.bind(productionQueue).to(exchange).with(productionRoutingKey);
    }

    @Bean
    public Binding assemblyBinding(TopicExchange exchange, Queue assemblyQueue) {
        return BindingBuilder.bind(assemblyQueue).to(exchange).with(assemblyRoutingKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public MessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory,
                                                             MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(assemblyQueueName);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    public MessageListenerAdapter messageListenerAdapter(AssemblyService assemblyService) {
        return new MessageListenerAdapter(assemblyService, "receiveComponent");
    }
}
