package com.uniflow.service;

import com.uniflow.dto.RealtimeMessage;
import com.uniflow.model.AcademicTrip;
import com.uniflow.model.Booking;
import com.uniflow.model.Notification;
import com.uniflow.model.TimetableEntry;
import com.uniflow.model.Venue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class RealtimeService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void broadcastBookingChange(RealtimeMessage.OperationType operation, Booking booking) {
        RealtimeMessage message = new RealtimeMessage(operation, RealtimeMessage.EntityType.BOOKING, booking);
        messagingTemplate.convertAndSend("/topic/bookings", message);
    }

    public void broadcastVenueChange(RealtimeMessage.OperationType operation, Venue venue) {
        RealtimeMessage message = new RealtimeMessage(operation, RealtimeMessage.EntityType.VENUE, venue);
        messagingTemplate.convertAndSend("/topic/venues", message);
    }

    public void broadcastTimetableChange(RealtimeMessage.OperationType operation, TimetableEntry timetableEntry) {
        RealtimeMessage message = new RealtimeMessage(operation, RealtimeMessage.EntityType.TIMETABLE, timetableEntry);
        messagingTemplate.convertAndSend("/topic/timetables", message);
    }

    public void broadcastTripChange(RealtimeMessage.OperationType operation, AcademicTrip trip) {
        RealtimeMessage message = new RealtimeMessage(operation, RealtimeMessage.EntityType.TRIP, trip);
        messagingTemplate.convertAndSend("/topic/trips", message);
    }

    public void broadcastNotificationChange(RealtimeMessage.OperationType operation, Notification notification) {
        RealtimeMessage message = new RealtimeMessage(operation, RealtimeMessage.EntityType.NOTIFICATION, notification);
        messagingTemplate.convertAndSend("/topic/notifications", message);
    }

    public void broadcastRequestChange(RealtimeMessage.OperationType operation, Object request) {
        RealtimeMessage message = new RealtimeMessage(operation, RealtimeMessage.EntityType.REQUEST, request);
        messagingTemplate.convertAndSend("/topic/requests", message);
    }
}