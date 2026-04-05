package com.uniflow.service;

import com.uniflow.dto.RealtimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * Broadcasts entity-change events to all connected WebSocket clients.
 *
 * <p>Each public method wraps the changed entity in a {@link RealtimeMessage}
 * and publishes it to the appropriate STOMP topic so that subscribed frontend
 * clients can update their local state without polling.
 *
 * <p>Topic mapping:
 * <pre>
 *   Booking          → /topic/bookings
 *   Venue            → /topic/venues
 *   TimetableEntry   → /topic/timetables
 *   AcademicTrip     → /topic/trips
 *   Notification     → /topic/notifications
 *   CourseUnitRequest→ /topic/requests
 * </pre>
 */
@Service
public class RealtimeService {

    private static final String TOPIC_BOOKINGS      = "/topic/bookings";
    private static final String TOPIC_VENUES        = "/topic/venues";
    private static final String TOPIC_TIMETABLES    = "/topic/timetables";
    private static final String TOPIC_TRIPS         = "/topic/trips";
    private static final String TOPIC_NOTIFICATIONS = "/topic/notifications";
    private static final String TOPIC_REQUESTS      = "/topic/requests";

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // ── Booking ──────────────────────────────────────────────────────────────

    public void broadcastBookingChange(String operationType, Object booking) {
        broadcast(TOPIC_BOOKINGS, operationType, "Booking", booking);
    }

    // ── Venue ────────────────────────────────────────────────────────────────

    public void broadcastVenueChange(String operationType, Object venue) {
        broadcast(TOPIC_VENUES, operationType, "Venue", venue);
    }

    // ── TimetableEntry ───────────────────────────────────────────────────────

    public void broadcastTimetableChange(String operationType, Object entry) {
        broadcast(TOPIC_TIMETABLES, operationType, "TimetableEntry", entry);
    }

    // ── AcademicTrip ─────────────────────────────────────────────────────────

    public void broadcastTripChange(String operationType, Object trip) {
        broadcast(TOPIC_TRIPS, operationType, "AcademicTrip", trip);
    }

    // ── Notification ─────────────────────────────────────────────────────────

    public void broadcastNotificationChange(String operationType, Object notification) {
        broadcast(TOPIC_NOTIFICATIONS, operationType, "Notification", notification);
    }

    // ── CourseUnitRequest ────────────────────────────────────────────────────

    public void broadcastRequestChange(String operationType, Object request) {
        broadcast(TOPIC_REQUESTS, operationType, "CourseUnitRequest", request);
    }

    // ── Internal helper ──────────────────────────────────────────────────────

    /**
     * Builds a {@link RealtimeMessage} and sends it to {@code destination}.
     *
     * @param destination  STOMP topic (e.g. {@code /topic/bookings})
     * @param operationType {@code CREATE}, {@code UPDATE}, or {@code DELETE}
     * @param entityType   human-readable entity name
     * @param data         the entity object (or a minimal id map for DELETE)
     */
    private void broadcast(String destination, String operationType, String entityType, Object data) {
        RealtimeMessage message = new RealtimeMessage(operationType, entityType, data);
        messagingTemplate.convertAndSend(destination, message);
    }
}
