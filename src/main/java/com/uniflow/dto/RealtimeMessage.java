package com.uniflow.dto;

import java.time.Instant;

/**
 * Payload broadcast over WebSocket STOMP topics whenever a database entity
 * is created, updated, or deleted.
 *
 * <p>Frontend subscribers receive JSON of the form:
 * <pre>
 * {
 *   "operationType": "CREATE",
 *   "entityType":    "Booking",
 *   "timestamp":     "2024-01-15T10:30:00Z",
 *   "data":          { ... entity fields ... }
 * }
 * </pre>
 */
public class RealtimeMessage {

    /** CREATE, UPDATE, or DELETE */
    private String operationType;

    /** Booking, Venue, TimetableEntry, AcademicTrip, Notification, CourseUnitRequest */
    private String entityType;

    /** ISO-8601 UTC timestamp of when the change occurred */
    private String timestamp;

    /** The serialised entity (or its id for DELETE events) */
    private Object data;

    public RealtimeMessage() {}

    public RealtimeMessage(String operationType, String entityType, Object data) {
        this.operationType = operationType;
        this.entityType    = entityType;
        this.timestamp     = Instant.now().toString();
        this.data          = data;
    }

    // ── Getters & Setters ────────────────────────────────────────────────────

    public String getOperationType() { return operationType; }
    public void setOperationType(String operationType) { this.operationType = operationType; }

    public String getEntityType() { return entityType; }
    public void setEntityType(String entityType) { this.entityType = entityType; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }
}
