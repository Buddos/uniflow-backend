package com.uniflow.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniflow.model.User;
import com.uniflow.service.AuthService;
import com.uniflow.util.CookieUtil;
import com.uniflow.util.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class AuthenticationServlet extends HttpServlet {
    
    @Autowired
    private AuthService authService;
    
    private ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getPathInfo();
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        try {
            if ("/login".equals(path)) {
                handleLogin(request, response, out);
            } else if ("/register".equals(path)) {
                handleRegister(request, response, out);
            } else if ("/logout".equals(path)) {
                handleLogout(request, response, out);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.write("{\"error\": \"Endpoint not found\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write(objectMapper.writeValueAsString(Map.of("error", e.getMessage())));
        }
    }
    
    private void handleRegister(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        
        User user = objectMapper.readValue(sb.toString(), User.class);
        User registeredUser = authService.register(user);
        SessionUtil.createSession(request, registeredUser);
        CookieUtil.createSessionCookie(response, request.getSession().getId());
        
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("success", true);
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", registeredUser.getId());
        userMap.put("name", registeredUser.getName());
        userMap.put("email", registeredUser.getEmail());
        userMap.put("role", registeredUser.getRole());
        userMap.put("department", registeredUser.getDepartment());
        responseData.put("user", userMap);
        
        out.write(objectMapper.writeValueAsString(responseData));
    }
    
    private void handleLogin(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        Map<String, String> loginData = objectMapper.readValue(sb.toString(), Map.class);
        User user = authService.authenticateUser(loginData.get("email"), loginData.get("password"));

        if (user != null) {
            SessionUtil.createSession(request, user);
            CookieUtil.createSessionCookie(response, request.getSession().getId());

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("success", true);
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("id", user.getId());
            userMap.put("name", user.getName());
            userMap.put("email", user.getEmail());
            userMap.put("role", user.getRole());
            userMap.put("department", user.getDepartment());
            responseData.put("user", userMap);

            out.write(objectMapper.writeValueAsString(responseData));
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.write(objectMapper.writeValueAsString(Map.of("error", "Invalid credentials")));
        }
    }
    
    private void handleLogout(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException {
        SessionUtil.invalidateSession(request);
        CookieUtil.clearAllCookies(request, response);
        
        Map<String, String> responseData = new HashMap<>();
        responseData.put("success", "true");
        responseData.put("message", "Logout successful");
        out.write(objectMapper.writeValueAsString(responseData));
    }
}