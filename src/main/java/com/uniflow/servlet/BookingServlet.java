package com.uniflow.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.uniflow.service.BookingService;
import com.uniflow.service.VenueService;
import com.uniflow.model.Booking;
import com.uniflow.model.Venue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BookingServlet extends HttpServlet {
    
    @Autowired
    private BookingService bookingService;

    @Autowired
    private VenueService venueService;
    
    private ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        try {
            List<?> bookings = bookingService.getAllBookings();
            out.write(objectMapper.writeValueAsString(bookings));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        try {
            JsonNode root = objectMapper.readTree(request.getReader());
            
            Booking booking = new Booking();
            
            // Map venue
            Long venueId = root.path("venueId").asLong();
            Venue venue = venueService.getVenueById(venueId).orElse(null);
            if (venue == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.write("{\"error\": \"Venue not found\"}");
                return;
            }
            booking.setVenue(venue);
            
            // Map times
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            booking.setStartTime(LocalDateTime.parse(root.path("startTime").asText(), formatter));
            booking.setEndTime(LocalDateTime.parse(root.path("endTime").asText(), formatter));
            
            booking.setPurpose(root.path("purpose").asText());
            booking.setStatus("CONFIRMED");
            
            Booking saved = bookingService.createBooking(booking);
            out.write(objectMapper.writeValueAsString(saved));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}
