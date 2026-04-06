package com.uniflow.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.uniflow.model.TimetableEntry;
import com.uniflow.service.TimetableService;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class TimetableServlet extends HttpServlet {

    @Autowired
    private TimetableService timetableService;

    private final ObjectMapper objectMapper;

    public TimetableServlet() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<TimetableEntry> entries;
        try {
            entries = timetableService.getAllTimetableEntries();
        } catch (Exception e) {
            entries = List.of();
        }

        // If this is a browser view request, forward to JSP
        String pathInfo = request.getPathInfo();
        String accept = request.getHeader("Accept");
        boolean isViewRequest = (pathInfo == null || "/".equals(pathInfo) || pathInfo.isEmpty())
                && (accept != null && accept.contains("text/html"));

        if (isViewRequest) {
            request.setAttribute("entries", entries);
            request.setAttribute("title", "Timetable Schedule - UniFlow");
            try {
                request.getRequestDispatcher("/WEB-INF/jsp/timetable.jsp").forward(request, response);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Error rendering timetable: " + e.getMessage());
            }
        } else {
            // Return JSON for API calls
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            try {
                out.write(objectMapper.writeValueAsString(entries));
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.write("{\"error\": \"" + e.getMessage() + "\"}");
            }
        }
    }
}
