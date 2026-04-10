package com.uniflow.service;

import com.uniflow.exception.ProximityViolationException;
import com.uniflow.model.Booking;
import com.uniflow.model.User;
import com.uniflow.model.Venue;
import com.uniflow.repository.BookingRepository;
import com.uniflow.repository.UserRepository;
import com.uniflow.repository.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookingService {
    
    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private VenueRepository venueRepository;
    
    public Booking createBooking(Booking booking) {
        assertWithinProximityLimit(booking.getVenue());

        // Check for conflicts
        List<Booking> conflicts = bookingRepository.findConflictingBookings(
            booking.getVenue(),
            booking.getStartTime(),
            booking.getEndTime()
        );
        
        if (!conflicts.isEmpty()) {
            throw new RuntimeException("Venue is already booked for the selected time slot");
        }
        
        booking.setStatus("CONFIRMED");
        booking.setCreatedAt(LocalDateTime.now());
        
        // Update venue status
        Venue venue = booking.getVenue();
        venue.setStatus("BOOKED");
        venueRepository.save(venue);
        
        return bookingRepository.save(booking);
    }
    
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
    
    public List<Booking> getUserBookings(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        return bookingRepository.findByBookedBy(user);
    }
    
    public List<Booking> getVenueBookings(Long venueId) {
        Venue venue = venueRepository.findById(venueId)
            .orElseThrow(() -> new RuntimeException("Venue not found"));
        return bookingRepository.findByVenue(venue);
    }
    
    public Booking cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        booking.setStatus("CANCELLED");
        
        // Update venue status
        Venue venue = booking.getVenue();
        venue.setStatus("AVAILABLE");
        venueRepository.save(venue);
        
        return bookingRepository.save(booking);
    }

    public Booking getBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found"));
    }
    
    public List<Booking> getAvailableVenuesForMakeupClass(LocalDateTime startTime, LocalDateTime endTime) {
        List<Venue> allVenues = venueRepository.findAll();
        List<Booking> availableBookings = new java.util.ArrayList<>();
        
        for (Venue venue : allVenues) {
            List<Booking> conflicts = bookingRepository.findConflictingBookings(venue, startTime, endTime);
            if (conflicts.isEmpty() && venue.getStatus().equals("AVAILABLE")) {
                Booking available = new Booking();
                available.setVenue(venue);
                availableBookings.add(available);
            }
        }

        availableBookings.sort(
            Comparator
                .comparing((Booking booking) -> !isWithinProximityLimit(booking.getVenue()))
                .thenComparing(booking -> booking.getVenue().getDistanceFromOfficeMeters(), Comparator.nullsLast(Integer::compareTo))
        );
        
        return availableBookings;
    }

    private void assertWithinProximityLimit(Venue venue) {
        if (venue == null || !isWithinProximityLimit(venue)) {
            throw new ProximityViolationException("Cannot assign venue: Location exceeds the 300m equipment proximity limit.");
        }
    }

    private boolean isWithinProximityLimit(Venue venue) {
        Integer distance = venue.getDistanceFromOfficeMeters();
        return distance != null && distance <= 300;
    }
    
    public Map<String, Object> getBookingStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", bookingRepository.count());
        stats.put("confirmed", bookingRepository.findByStatus("CONFIRMED").size());
        stats.put("cancelled", bookingRepository.findByStatus("CANCELLED").size());
        stats.put("completed", bookingRepository.findByStatus("COMPLETED").size());
        stats.put("makeup", bookingRepository.findByBookingType("MAKEUP").size());
        return stats;
    }
}