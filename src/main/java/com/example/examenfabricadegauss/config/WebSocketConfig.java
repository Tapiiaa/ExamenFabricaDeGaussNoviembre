package com.example.examenfabricadegauss.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");    // Configura un broker para enviar mensajes a los clientes en el prefijo /topic
        config.setApplicationDestinationPrefixes("/app");   // Configura un prefijo para los mensajes enviados desde los clientes
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Registra un punto final para el cliente WebSocket con soporte de fallback de SockJS
        registry.addEndpoint("/gauss-websocket").withSockJS();
    }

}
