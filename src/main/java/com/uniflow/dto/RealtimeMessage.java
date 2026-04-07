package com.uniflow.dto;

import java.time.LocalDateTime;

public class RealtimeMessage {
    public enum OperationType {
        CREATE, UPDATE, DELETE
    }

    public enum EntityType {
        BOOKING, VENUE, TIMETABLE, TRIP, NOTIFICATION, REQUEST
    }

    private OperationType operation;
    private EntityType entityType;
    private String timestamp;
    private Object payload;

    public RealtimeMessage() {}

    public RealtimeMessage(OperationType operation, EntityType entityType, Object payload) {
        this.operation = operation;
        this.entityType = entityType;
        this.timestamp = LocalDateTime.now().toString();
        this.payload = payload;
    }

    // Getters and setters
    public OperationType getOperation() {
        return operation;
    }

    public void setOperation(OperationType operation) {
        this.operation = operation;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }
}