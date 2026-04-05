package com.uniflow.dto;

import java.time.LocalDateTime;

public class EntityUpdateMessage {

    public enum Action {
        CREATE, UPDATE, DELETE
    }

    private String entityType;
    private Action action;
    private Object data;
    private Long entityId;
    private LocalDateTime timestamp;

    public EntityUpdateMessage() {
        this.timestamp = LocalDateTime.now();
    }

    public EntityUpdateMessage(String entityType, Action action, Object data) {
        this.entityType = entityType;
        this.action = action;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public EntityUpdateMessage(String entityType, Action action, Object data, Long entityId) {
        this.entityType = entityType;
        this.action = action;
        this.data = data;
        this.entityId = entityId;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public String getEntityType() { return entityType; }
    public void setEntityType(String entityType) { this.entityType = entityType; }

    public Action getAction() { return action; }
    public void setAction(Action action) { this.action = action; }

    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }

    public Long getEntityId() { return entityId; }
    public void setEntityId(Long entityId) { this.entityId = entityId; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
