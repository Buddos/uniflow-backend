package com.uniflow.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    
    private ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Skip OPTIONS requests
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("user") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            Map<String, String> error = new HashMap<>();
            error.put("error", "Unauthorized. Please login.");
            response.getWriter().write(objectMapper.writeValueAsString(error));
            return false;
        }
        
        // Check session timeout
        Long lastActivity = (Long) session.getAttribute("lastActivity");
        if (lastActivity != null) {
            long inactivityTime = System.currentTimeMillis() - lastActivity;
            if (inactivityTime > 30 * 60 * 1000) { // 30 minutes
                session.invalidate();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                Map<String, String> error = new HashMap<>();
                error.put("error", "Session expired. Please login again.");
                response.getWriter().write(objectMapper.writeValueAsString(error));
                return false;
            }
        }
        
        // Update last activity
        session.setAttribute("lastActivity", System.currentTimeMillis());
        return true;
    }
}