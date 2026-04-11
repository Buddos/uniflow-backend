package com.uniflow.servlet;

import com.uniflow.model.AcademicTrip;
import com.uniflow.model.Venue;
import com.uniflow.service.TripService;
import com.uniflow.service.VenueService;
import com.uniflow.util.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class VenueServlet extends HttpServlet {
    
    @Autowired
    private VenueService venueService;

    @Autowired
    private TripService tripService;
    
    private static final String LIVE_MAP_VIEW = "/WEB-INF/jsp/liveMap.jsp";
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");

        try {
            if (!isAcademicRole(SessionUtil.getUserRole(request))) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Forbidden. You do not have permission to access this resource.\"}");
                return;
            }
            List<Venue> venueList = venueService.getAllVenues();
            List<AcademicTrip> activeTrips = tripService.getActiveTrips();

            request.setAttribute("venues", venueList);
            request.setAttribute("activeTrips", activeTrips);
            request.setAttribute("greenVenues", venueList.stream()
                .filter(venue -> "AVAILABLE".equalsIgnoreCase(venue.getStatus()))
                .collect(Collectors.toList()));
            request.setAttribute("title", "Live Map - UniFlow");
            request.getRequestDispatcher(LIVE_MAP_VIEW).forward(request, response);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private boolean isAcademicRole(String role) {
        if (role == null) {
            return false;
        }

        String normalizedRole = role.toUpperCase();
        return "STUDENT".equals(normalizedRole) || "COD".equals(normalizedRole) || "LECTURER".equals(normalizedRole);
    }
}