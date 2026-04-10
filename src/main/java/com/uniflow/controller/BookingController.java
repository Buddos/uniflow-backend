package com.uniflow.controller;

import com.uniflow.dto.BookingRequest;
import com.uniflow.dto.RealtimeMessage;
import com.uniflow.exception.UnauthorizedException;
import com.uniflow.model.Booking;
import com.uniflow.model.User;
import com.uniflow.model.Venue;
import com.uniflow.repository.UserRepository;
import com.uniflow.repository.VenueRepository;
import com.uniflow.service.BookingService;
import com.uniflow.service.QRCodeGeneratorService;
import com.uniflow.service.RealtimeService;
import com.uniflow.util.SessionUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    
    @Autowired
    private BookingService bookingService;
    
    @Autowired
    private VenueRepository venueRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RealtimeService realtimeService;

    @Autowired
    private QRCodeGeneratorService qrCodeGeneratorService;
    
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
    public ResponseEntity<Booking> createBooking(@Valid @RequestBody BookingRequest bookingRequest, HttpServletRequest request) {
        String role = SessionUtil.getUserRole(request);
        if ("LECTURER".equalsIgnoreCase(role)
                && !"MAKEUP".equalsIgnoreCase(bookingRequest.getBookingType())) {
            throw new UnauthorizedException("Lecturers may only create makeup bookings.");
        }

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
        
        Booking savedBooking = bookingService.bookMakeupClass(booking);
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

    @GetMapping("/{bookingId}/voucher")
    public ResponseEntity<Map<String, Object>> getVoucher(@PathVariable Long bookingId, HttpServletRequest request) {
        String role = SessionUtil.getUserRole(request);
        if (!"CLASS_REP".equalsIgnoreCase(role)) {
            throw new UnauthorizedException("Only Class Reps can access booking vouchers.");
        }

        Booking booking = bookingService.getBookingById(bookingId);

        String payload = "bookingId=" + booking.getId()
            + "|venueName=" + booking.getVenue().getName()
            + "|equipmentOfficeName=" + booking.getVenue().getEquipmentOfficeName()
            + "|scheduledEndTime=" + booking.getEndTime()
            + "|generatedAt=" + LocalDateTime.now();

        String qrCodeBase64 = qrCodeGeneratorService.generateQrCodeBase64(payload);

        Map<String, Object> response = new HashMap<>();
        response.put("bookingId", booking.getId());
        response.put("payload", payload);
        response.put("qrCodeBase64", qrCodeBase64);
        return ResponseEntity.ok(response);
    }
}