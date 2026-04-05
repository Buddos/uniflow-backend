package com.uniflow.dto;

import java.time.LocalDateTime;

public class WebSocketMessage<T> {

    private String type;
    private T payload;
    private LocalDateTime timestamp;
    private String source;

    public WebSocketMessage() {
        this.timestamp = LocalDateTime.now();
    }

    public WebSocketMessage(String type, T payload) {
        this.type = type;
        this.payload = payload;
        this.timestamp = LocalDateTime.now();
    }

    public WebSocketMessage(String type, T payload, String source) {
        this.type = type;
        this.payload = payload;
        this.source = source;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public T getPayload() { return payload; }
    public void setPayload(T payload) { this.payload = payload; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
}
