package com.uniflow.controller;

import com.uniflow.dto.BookingRequest;
import com.uniflow.dto.RealtimeMessage;
import com.uniflow.model.Booking;
import com.uniflow.model.User;
import com.uniflow.model.Venue;
import com.uniflow.repository.UserRepository;
import com.uniflow.repository.VenueRepository;
import com.uniflow.service.BookingService;
import com.uniflow.service.RealtimeService;
import com.uniflow.util.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {
    
    @Autowired
    private BookingService bookingService;
    
    @Autowired
    private VenueRepository venueRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RealtimeService realtimeService;
    
    @GetMapping("/my")
    public ResponseEntity<List<Booking>> getMyBookings(HttpServletRequest request) {
        Long userId = SessionUtil.getCurrentUserId(request);
        return ResponseEntity.ok(bookingService.getUserBookings(userId));
    }
    
    @GetMapping("/venue/{venueId}")
    public ResponseEntity<List<Booking>> getVenueBookings(@PathVariable Long venueId) {
        return ResponseEntity.ok(bookingService.getVenueBookings(venueId));
    }
    
    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody BookingRequest bookingRequest, HttpServletRequest request) {
        Booking booking = new Booking();
        
        Venue venue = venueRepository.findById(bookingRequest.getVenueId())
            .orElseThrow(() -> new RuntimeException("Venue not found"));
        booking.setVenue(venue);
        
        booking.setStartTime(bookingRequest.getStartTime());
        booking.setEndTime(bookingRequest.getEndTime());
        booking.setPurpose(bookingRequest.getPurpose());
        booking.setBookingType(bookingRequest.getBookingType());
        
        Long userId = SessionUtil.getCurrentUserId(request);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        booking.setBookedBy(user);
        
        Booking savedBooking = bookingService.createBooking(booking);
        realtimeService.broadcastBookingChange(RealtimeMessage.OperationType.CREATE, savedBooking);
        return ResponseEntity.ok(savedBooking);
    }
    
    @PostMapping("/{bookingId}/cancel")
    public ResponseEntity<Booking> cancelBooking(@PathVariable Long bookingId) {
        Booking cancelledBooking = bookingService.cancelBooking(bookingId);
        realtimeService.broadcastBookingChange(RealtimeMessage.OperationType.UPDATE, cancelledBooking);
        return ResponseEntity.ok(cancelledBooking);
    }
    
    @GetMapping("/available-makeup")
    public ResponseEntity<List<Booking>> getAvailableMakeupSlots(
            @RequestParam String startTime,
            @RequestParam String endTime) {
        LocalDateTime start = LocalDateTime.parse(startTime);
        LocalDateTime end = LocalDateTime.parse(endTime);
        return ResponseEntity.ok(bookingService.getAvailableVenuesForMakeupClass(start, end));
    }
    
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        return ResponseEntity.ok(bookingService.getBookingStatistics());
    }
}