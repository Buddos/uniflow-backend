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

@Component
public class AuthServlet extends HttpServlet {
    
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
            out.write("{\"error\": \"" + e.getMessage() + "\"}");
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
        
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("success", true);
        responseData.put("user", Map.of(
            "id", registeredUser.getId(),
            "name", registeredUser.getName(),
            "email", registeredUser.getEmail(),
            "role", registeredUser.getRole()
        ));
        
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
            responseData.put("user", Map.of(
                "id", user.getId(),
                "name", user.getName(),
                "email", user.getEmail(),
                "role", user.getRole()
            ));
            
            out.write(objectMapper.writeValueAsString(responseData));
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.write("{\"error\": \"Invalid credentials\"}");
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