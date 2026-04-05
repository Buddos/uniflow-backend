package com.uniflow.service;

import com.uniflow.dto.EntityUpdateMessage;
import com.uniflow.dto.EntityUpdateMessage.Action;
import com.uniflow.model.AcademicTrip;
import com.uniflow.model.Booking;
import com.uniflow.model.CourseUnit;
import com.uniflow.model.Notification;
import com.uniflow.model.TimetableEntry;
import com.uniflow.model.User;
import com.uniflow.model.Venue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * Publishes entity change events to WebSocket topic destinations so that all
 * connected STOMP clients receive real-time updates whenever data is created,
 * updated, or deleted.
 */
@Service
public class DataChangePublisher {

    private static final String TOPIC_BOOKINGS       = "/topic/bookings";
    private static final String TOPIC_TIMETABLES     = "/topic/timetables";
    private static final String TOPIC_COURSES        = "/topic/courses";
    private static final String TOPIC_VENUES         = "/topic/venues";
    private static final String TOPIC_USERS          = "/topic/users";
    private static final String TOPIC_NOTIFICATIONS  = "/topic/notifications";
    private static final String TOPIC_TRIPS          = "/topic/trips";

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // -------------------------------------------------------------------------
    // Booking events
    // -------------------------------------------------------------------------

    public void publishBookingCreated(Booking booking) {
        send(TOPIC_BOOKINGS, new EntityUpdateMessage("Booking", Action.CREATE, booking, booking.getId()));
    }

    public void publishBookingUpdated(Booking booking) {
        send(TOPIC_BOOKINGS, new EntityUpdateMessage("Booking", Action.UPDATE, booking, booking.getId()));
    }

    public void publishBookingDeleted(Long bookingId) {
        send(TOPIC_BOOKINGS, new EntityUpdateMessage("Booking", Action.DELETE, null, bookingId));
    }

    // -------------------------------------------------------------------------
    // TimetableEntry events
    // -------------------------------------------------------------------------

    public void publishTimetableEntryCreated(TimetableEntry entry) {
        send(TOPIC_TIMETABLES, new EntityUpdateMessage("TimetableEntry", Action.CREATE, entry, entry.getId()));
    }

    public void publishTimetableEntryUpdated(TimetableEntry entry) {
        send(TOPIC_TIMETABLES, new EntityUpdateMessage("TimetableEntry", Action.UPDATE, entry, entry.getId()));
    }

    public void publishTimetableEntryDeleted(Long entryId) {
        send(TOPIC_TIMETABLES, new EntityUpdateMessage("TimetableEntry", Action.DELETE, null, entryId));
    }

    // -------------------------------------------------------------------------
    // CourseUnit events
    // -------------------------------------------------------------------------

    public void publishCourseUnitCreated(CourseUnit courseUnit) {
        send(TOPIC_COURSES, new EntityUpdateMessage("CourseUnit", Action.CREATE, courseUnit, courseUnit.getId()));
    }

    public void publishCourseUnitUpdated(CourseUnit courseUnit) {
        send(TOPIC_COURSES, new EntityUpdateMessage("CourseUnit", Action.UPDATE, courseUnit, courseUnit.getId()));
    }

    public void publishCourseUnitDeleted(Long courseUnitId) {
        send(TOPIC_COURSES, new EntityUpdateMessage("CourseUnit", Action.DELETE, null, courseUnitId));
    }

    // -------------------------------------------------------------------------
    // Venue events
    // -------------------------------------------------------------------------

    public void publishVenueCreated(Venue venue) {
        send(TOPIC_VENUES, new EntityUpdateMessage("Venue", Action.CREATE, venue, venue.getId()));
    }

    public void publishVenueUpdated(Venue venue) {
        send(TOPIC_VENUES, new EntityUpdateMessage("Venue", Action.UPDATE, venue, venue.getId()));
    }

    public void publishVenueDeleted(Long venueId) {
        send(TOPIC_VENUES, new EntityUpdateMessage("Venue", Action.DELETE, null, venueId));
    }

    // -------------------------------------------------------------------------
    // User events
    // -------------------------------------------------------------------------

    public void publishUserCreated(User user) {
        send(TOPIC_USERS, new EntityUpdateMessage("User", Action.CREATE, user, user.getId()));
    }

    public void publishUserUpdated(User user) {
        send(TOPIC_USERS, new EntityUpdateMessage("User", Action.UPDATE, user, user.getId()));
    }

    public void publishUserDeleted(Long userId) {
        send(TOPIC_USERS, new EntityUpdateMessage("User", Action.DELETE, null, userId));
    }

    // -------------------------------------------------------------------------
    // Notification events
    // -------------------------------------------------------------------------

    public void publishNotificationCreated(Notification notification) {
        send(TOPIC_NOTIFICATIONS, new EntityUpdateMessage("Notification", Action.CREATE, notification, notification.getId()));
    }

    public void publishNotificationUpdated(Notification notification) {
        send(TOPIC_NOTIFICATIONS, new EntityUpdateMessage("Notification", Action.UPDATE, notification, notification.getId()));
    }

    // -------------------------------------------------------------------------
    // AcademicTrip events
    // -------------------------------------------------------------------------

    public void publishTripCreated(AcademicTrip trip) {
        send(TOPIC_TRIPS, new EntityUpdateMessage("AcademicTrip", Action.CREATE, trip, trip.getId()));
    }

    public void publishTripUpdated(AcademicTrip trip) {
        send(TOPIC_TRIPS, new EntityUpdateMessage("AcademicTrip", Action.UPDATE, trip, trip.getId()));
    }

    public void publishTripDeleted(Long tripId) {
        send(TOPIC_TRIPS, new EntityUpdateMessage("AcademicTrip", Action.DELETE, null, tripId));
    }

    // -------------------------------------------------------------------------
    // Internal helper
    // -------------------------------------------------------------------------

    private void send(String destination, EntityUpdateMessage message) {
        messagingTemplate.convertAndSend(destination, message);
    }
}
