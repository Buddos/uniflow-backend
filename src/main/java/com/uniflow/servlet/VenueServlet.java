package com.uniflow.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniflow.model.Venue;
import com.uniflow.service.VenueService;
import com.uniflow.util.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "VenueServlet", urlPatterns = {"/servlet/venues/*"})
@Component
public class VenueServlet extends HttpServlet {
    
    @Autowired
    private VenueService venueService;
    
    private ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getPathInfo();
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        try {
            if ("/all".equals(path) || path == null) {
                List<Venue> venues = venueService.getAllVenues();
                out.write(objectMapper.writeValueAsString(venues));
            } else if ("/available".equals(path)) {
                List<Venue> venues = venueService.getAvailableVenues();
                out.write(objectMapper.writeValueAsString(venues));
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.write("{\"error\": \"Endpoint not found\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userRole = SessionUtil.getUserRole(request);
        if (!"ADMIN".equals(userRole)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        
        Venue venue = objectMapper.readValue(sb.toString(), Venue.class);
        Venue created = venueService.createVenue(venue);
        
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(created));
    }
}