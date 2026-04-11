package com.uniflow.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniflow.dto.RealtimeMessage;
import com.uniflow.dto.TripRequestDTO;
import com.uniflow.model.AcademicTrip;
import com.uniflow.model.Venue;
import com.uniflow.service.RealtimeService;
import com.uniflow.service.TripService;
import com.uniflow.service.VenueService;
import com.uniflow.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

public class TripServlet extends HttpServlet {

    @Autowired
    private TripService tripService;

    @Autowired
    private VenueService venueService;

    @Autowired
    private RealtimeService realtimeService;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");

        try {
            if (!"LECTURER".equalsIgnoreCase(SessionUtil.getUserRole(request))) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Forbidden. Lecturers only.\"}");
                return;
            }

            StringBuilder body = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                body.append(line);
            }

            TripRequestDTO tripDTO = objectMapper.readValue(body.toString(), TripRequestDTO.class);
            AcademicTrip trip = new AcademicTrip();
            trip.setTitle(tripDTO.getTitle());
            trip.setDestination(tripDTO.getDestination());
            trip.setCohort(tripDTO.getCohort());
            trip.setStartDate(tripDTO.getStartDate());
            trip.setEndDate(tripDTO.getEndDate());
            trip.setDescription(tripDTO.getDescription());
            trip.setDepartment(tripDTO.getDepartment());
            trip.setNumberOfStudents(tripDTO.getNumberOfStudents());

            AcademicTrip savedTrip = tripService.createTrip(trip);
            realtimeService.broadcastTripChange(RealtimeMessage.OperationType.CREATE, savedTrip);

            List<Venue> venueList = venueService.getAllVenues();
            request.setAttribute("venues", venueList);
            request.setAttribute("activeTrips", tripService.getActiveTrips());
            request.setAttribute("greenVenues", venueList.stream()
                .filter(venue -> "AVAILABLE".equalsIgnoreCase(venue.getStatus()))
                .collect(Collectors.toList()));
            request.setAttribute("title", "Live Map - UniFlow");
            request.setAttribute("message", "Trip logged successfully. Released rooms have been refreshed.");
            request.getRequestDispatcher("/WEB-INF/jsp/liveMap.jsp").forward(request, response);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}
