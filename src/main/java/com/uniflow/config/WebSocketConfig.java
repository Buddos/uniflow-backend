package com.uniflow.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Configures STOMP-over-WebSocket support.
 *
 * <p>Clients connect to {@code /ws} (with SockJS fallback) and subscribe to
 * topics under the {@code /topic} prefix.  The application sends messages to
 * those topics via {@link org.springframework.messaging.simp.SimpMessagingTemplate}.
 *
 * <p>Supported topics:
 * <ul>
 *   <li>{@code /topic/bookings}
 *   <li>{@code /topic/venues}
 *   <li>{@code /topic/timetables}
 *   <li>{@code /topic/trips}
 *   <li>{@code /topic/notifications}
 *   <li>{@code /topic/requests}
 * </ul>
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private static final String[] ALLOWED_ORIGINS = {
            "http://localhost:3000",
            "http://localhost:5173",
            "http://localhost:8080"
    };

    /**
     * Register the STOMP endpoint that clients connect to.
     * SockJS is enabled as a fallback for environments that do not support
     * native WebSockets.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins(ALLOWED_ORIGINS)
                .withSockJS();
    }

    /**
     * Configure the in-memory message broker.
     *
     * <ul>
     *   <li>{@code /topic} – server-to-client broadcast destinations
     *   <li>{@code /app}   – client-to-server message destinations (reserved
     *       for future use; not currently consumed by any {@code @MessageMapping})
     * </ul>
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }
}
