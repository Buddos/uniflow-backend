package com.uniflow.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private static final Set<String> TIMETABLE_WRITE_METHODS = Set.of("POST", "PUT", "PATCH", "DELETE");
    private static final Pattern BOOKING_VOUCHER_PATTERN = Pattern.compile("^/api/bookings/\\d+/voucher$");
    
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

        String path = request.getRequestURI();
        String method = request.getMethod().toUpperCase();
        String role = String.valueOf(session.getAttribute("userRole")).toUpperCase();

        if (!isRoleAllowed(path, method, role)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            Map<String, String> error = new HashMap<>();
            error.put("error", "Forbidden. You do not have permission to access this resource.");
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

    private boolean isRoleAllowed(String path, String method, String role) {
        if (BOOKING_VOUCHER_PATTERN.matcher(path).matches()) {
            return "CLASS_REP".equals(role);
        }

        if (path.startsWith("/api/timetable") && TIMETABLE_WRITE_METHODS.contains(method)) {
            return "COD".equals(role) || "DET".equals(role);
        }

        if (path.startsWith("/api/requests/admin/enforce-deadline")) {
            return "COD".equals(role) || "DET".equals(role);
        }

        if ("LECTURER".equals(role)) {
            return path.startsWith("/api/trips")
                || path.startsWith("/api/bookings/my")
                || path.startsWith("/api/bookings/available-makeup")
                || (path.equals("/api/bookings") && "POST".equals(method));
        }

        if ("CLASS_REP".equals(role)) {
            return BOOKING_VOUCHER_PATTERN.matcher(path).matches()
                || path.equals("/api/auth/check-session")
                || path.equals("/api/auth/logout");
        }

        return true;
    }
}