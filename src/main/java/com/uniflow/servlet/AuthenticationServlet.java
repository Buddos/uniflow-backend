package com.uniflow.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniflow.model.User;
import com.uniflow.service.AuthService;
import com.uniflow.util.CookieUtil;
import com.uniflow.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AuthenticationServlet extends HttpServlet {

    private static final Set<String> ACADEMIC_ROLES = Set.of("STUDENT", "COD", "LECTURER");

    @Autowired
    private AuthService authService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String path = request.getPathInfo();
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try {
            if ("/login".equals(path)) {
                handleLogin(request, response);
            } else if ("/register".equals(path)) {
                handleRegister(request, response, out);
            } else if ("/logout".equals(path)) {
                handleLogout(request, response);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.write("{\"error\": \"Endpoint not found\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write(objectMapper.writeValueAsString(Map.of("error", e.getMessage())));
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String path = request.getPathInfo();
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try {
            if ("/logout".equals(path)) {
                handleLogout(request, response);
            } else if ("/check-session".equals(path)) {
                handleCheckSession(request, out);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.write("{\"error\": \"Endpoint not found\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write(objectMapper.writeValueAsString(Map.of("error", e.getMessage())));
        }
    }

    private void handleRegister(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws IOException, ServletException {
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

        request.setAttribute("title", "UniFlow - Dashboard Home");
        request.setAttribute("activeUser", registeredUser);
        request.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(request, response);
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
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

            request.setAttribute("title", "UniFlow - Dashboard Home");
            request.setAttribute("activeUser", user);
            if (user.getRole() != null && ACADEMIC_ROLES.contains(user.getRole().toUpperCase())) {
                request.setAttribute("authRole", user.getRole());
            }

            request.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(objectMapper.writeValueAsString(Map.of("error", "Invalid credentials")));
        }
    }

    private void handleLogout(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String role = SessionUtil.getUserRole(request);
        if (role != null && !ACADEMIC_ROLES.contains(role.toUpperCase())) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write(objectMapper.writeValueAsString(Map.of("error", "Forbidden.")));
            return;
        }

        SessionUtil.invalidateSession(request);
        CookieUtil.clearAllCookies(request, response);

        request.setAttribute("title", "UniFlow - Dashboard Home");
        request.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(request, response);
    }

    private void handleCheckSession(HttpServletRequest request, PrintWriter out) throws IOException {
        boolean isValid = SessionUtil.isSessionValid(request);
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("valid", isValid);

        if (isValid) {
            User user = SessionUtil.getCurrentUser(request);
            if (user != null) {
                String role = user.getRole();
                if (role != null && ACADEMIC_ROLES.contains(role.toUpperCase())) {
                    responseData.put("user", Map.of(
                        "name", user.getName(),
                        "email", user.getEmail(),
                        "role", user.getRole()
                    ));
                }
            }
        }

        out.write(objectMapper.writeValueAsString(responseData));
    }
}
