package com.uniflow.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Enable a simple in-memory broker for broadcasting to /topic destinations
        registry.enableSimpleBroker("/topic");

        // Messages sent from clients with the /app prefix are routed to @MessageMapping methods
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Primary STOMP endpoint — clients connect here to establish a WebSocket session
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();

        // Raw WebSocket endpoint (no SockJS fallback) for clients that support it natively
        registry.addEndpoint("/ws-native")
                .setAllowedOriginPatterns("*");
    }
}
