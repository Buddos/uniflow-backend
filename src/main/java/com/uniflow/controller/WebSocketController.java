package com.uniflow.controller;

import com.uniflow.dto.EntityUpdateMessage;
import com.uniflow.dto.WebSocketMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 * Handles inbound STOMP messages from connected clients and broadcasts
 * entity-change events to the appropriate topic destinations.
 *
 * Client-side usage (STOMP):
 *   stompClient.subscribe('/topic/bookings',   handler);
 *   stompClient.subscribe('/topic/timetables', handler);
 *   stompClient.subscribe('/topic/courses',    handler);
 *   stompClient.subscribe('/topic/venues',     handler);
 *   stompClient.subscribe('/topic/users',      handler);
 *   stompClient.subscribe('/topic/notifications', handler);
 *   stompClient.subscribe('/topic/trips',      handler);
 *
 *   // Send an update from the client:
 *   stompClient.send('/app/booking.update', {}, JSON.stringify(message));
 */
@Controller
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // -------------------------------------------------------------------------
    // Booking messages
    // -------------------------------------------------------------------------

    /**
     * Receives a booking change message from a client and broadcasts it to all
     * subscribers of /topic/bookings.
     */
    @MessageMapping("/booking.update")
    @SendTo("/topic/bookings")
    public EntityUpdateMessage handleBookingUpdate(WebSocketMessage<EntityUpdateMessage> message) {
        return message.getPayload();
    }

    // -------------------------------------------------------------------------
    // Timetable messages
    // -------------------------------------------------------------------------

    @MessageMapping("/timetable.update")
    @SendTo("/topic/timetables")
    public EntityUpdateMessage handleTimetableUpdate(WebSocketMessage<EntityUpdateMessage> message) {
        return message.getPayload();
    }

    // -------------------------------------------------------------------------
    // Course unit messages
    // -------------------------------------------------------------------------

    @MessageMapping("/course.update")
    @SendTo("/topic/courses")
    public EntityUpdateMessage handleCourseUpdate(WebSocketMessage<EntityUpdateMessage> message) {
        return message.getPayload();
    }

    // -------------------------------------------------------------------------
    // Venue messages
    // -------------------------------------------------------------------------

    @MessageMapping("/venue.update")
    @SendTo("/topic/venues")
    public EntityUpdateMessage handleVenueUpdate(WebSocketMessage<EntityUpdateMessage> message) {
        return message.getPayload();
    }

    // -------------------------------------------------------------------------
    // User messages
    // -------------------------------------------------------------------------

    @MessageMapping("/user.update")
    @SendTo("/topic/users")
    public EntityUpdateMessage handleUserUpdate(WebSocketMessage<EntityUpdateMessage> message) {
        return message.getPayload();
    }

    // -------------------------------------------------------------------------
    // Notification messages
    // -------------------------------------------------------------------------

    @MessageMapping("/notification.update")
    @SendTo("/topic/notifications")
    public EntityUpdateMessage handleNotificationUpdate(WebSocketMessage<EntityUpdateMessage> message) {
        return message.getPayload();
    }

    // -------------------------------------------------------------------------
    // Academic trip messages
    // -------------------------------------------------------------------------

    @MessageMapping("/trip.update")
    @SendTo("/topic/trips")
    public EntityUpdateMessage handleTripUpdate(WebSocketMessage<EntityUpdateMessage> message) {
        return message.getPayload();
    }
}
