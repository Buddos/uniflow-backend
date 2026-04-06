package com.uniflow.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.uniflow.model.CourseUnitRequest;
import com.uniflow.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class RequestServlet extends HttpServlet {

    @Autowired
    private RequestService requestService;

    private final ObjectMapper objectMapper;

    public RequestServlet() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<CourseUnitRequest> requests;
        try {
            requests = requestService.getAllRequests();
        } catch (Exception e) {
            requests = List.of();
        }

        String pathInfo = request.getPathInfo();
        String accept = request.getHeader("Accept");
        boolean isViewRequest = (pathInfo == null || "/".equals(pathInfo) || pathInfo.isEmpty())
                && (accept != null && accept.contains("text/html"));

        if (isViewRequest) {
            request.setAttribute("requests", requests);
            request.setAttribute("title", "Course Unit Requests - UniFlow");
            try {
                request.getRequestDispatcher("/WEB-INF/jsp/courserequest.jsp").forward(request, response);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Error rendering course requests page: " + e.getMessage());
            }
        } else {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            try {
                out.write(objectMapper.writeValueAsString(requests));
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.write("{\"error\": \"" + e.getMessage() + "\"}");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            String contentType = request.getContentType();

            CourseUnitRequest req = new CourseUnitRequest();

            if (contentType != null && contentType.contains("application/json")) {
                // JSON API request (from React or Postman)
                JsonNode root = objectMapper.readTree(request.getReader());
                req.setRequestingDepartment(root.path("requestingDept").asText());
                req.setProvidingDepartment(root.path("providingDept").asText());
                req.setExpectedStudents(root.path("cohortSize").asInt());
                req.setComments(root.path("courseUnit").asText());
                req.setSemester(root.path("semester").asText("2024/2025 Sem 1"));
            } else {
                // Form submission from JSP page
                req.setRequestingDepartment(request.getParameter("requestingDept"));
                req.setProvidingDepartment(request.getParameter("providingDept"));
                String cohortSize = request.getParameter("cohortSize");
                if (cohortSize != null && !cohortSize.isEmpty()) {
                    req.setExpectedStudents(Integer.parseInt(cohortSize));
                }
                req.setComments(request.getParameter("courseUnitId"));
                req.setSemester("2024/2025 Sem 1");
            }

            req.setStatus("PENDING");
            CourseUnitRequest saved = requestService.createRequest(req);

            // If it was a form submission, redirect back to the view page
            if (contentType == null || !contentType.contains("application/json")) {
                response.sendRedirect(request.getContextPath() + "/view/courserequest");
                return;
            }

            out.write(objectMapper.writeValueAsString(saved));

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}
