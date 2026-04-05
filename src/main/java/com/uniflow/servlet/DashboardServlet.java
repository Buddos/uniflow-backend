package com.uniflow.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniflow.service.*;
import com.uniflow.util.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Component
public class DashboardServlet extends HttpServlet {
    
    @Autowired
    private VenueService venueService;
    
    @Autowired
    private RequestService requestService;
    
    @Autowired
    private TripService tripService;
    
    @Autowired
    private BookingService bookingService;
    
    private ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getPathInfo();
        String userRole = SessionUtil.getUserRole(request);
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        Map<String, Object> dashboard = new HashMap<>();
        
        if ("/admin".equals(path) && "ADMIN".equals(userRole)) {
            dashboard.put("venues", venueService.getVenueStatistics());
            dashboard.put("requests", requestService.getRequestStatistics());
            dashboard.put("trips", tripService.getTripStatistics());
            dashboard.put("bookings", bookingService.getBookingStatistics());
        } else if ("/cod".equals(path) && "COD".equals(userRole)) {
            String department = (String) request.getSession().getAttribute("department");
            dashboard.put("pendingRequests", requestService.getPendingRequestsForDepartment(department).size());
            dashboard.put("departmentTrips", tripService.getTripsByDepartment(department).size());
        } else if ("/lecturer".equals(path) && "LECTURER".equals(userRole)) {
            Long userId = SessionUtil.getCurrentUserId(request);
            dashboard.put("myBookings", bookingService.getUserBookings(userId).size());
            dashboard.put("notifications", 0); // Would fetch from notification service
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            out.write("{\"error\": \"Access denied\"}");
            return;
        }
        
        out.write(objectMapper.writeValueAsString(dashboard));
    }
}