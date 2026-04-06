package com.uniflow.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.uniflow.model.Equipment;
import com.uniflow.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class EquipmentServlet extends HttpServlet {

    @Autowired
    private EquipmentService equipmentService;

    private final ObjectMapper objectMapper;

    public EquipmentServlet() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Equipment> equipmentList;
        try {
            equipmentList = equipmentService.getAllEquipment();
        } catch (Exception e) {
            equipmentList = List.of();
        }

        String pathInfo = request.getPathInfo();
        String accept = request.getHeader("Accept");
        boolean isViewRequest = (pathInfo == null || "/".equals(pathInfo) || pathInfo.isEmpty())
                && (accept != null && accept.contains("text/html"));

        if (isViewRequest) {
            request.setAttribute("equipmentList", equipmentList);
            request.setAttribute("title", "Equipment Tracking - UniFlow");
            try {
                request.getRequestDispatcher("/WEB-INF/jsp/equipment.jsp").forward(request, response);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Error rendering equipment page: " + e.getMessage());
            }
        } else {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            try {
                out.write(objectMapper.writeValueAsString(equipmentList));
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
            Equipment equipment = objectMapper.readValue(request.getReader(), Equipment.class);
            Equipment created = equipmentService.createEquipment(equipment);
            response.setStatus(HttpServletResponse.SC_CREATED);
            out.write(objectMapper.writeValueAsString(created));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}
