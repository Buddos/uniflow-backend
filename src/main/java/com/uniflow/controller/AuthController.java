package com.uniflow.controller;

import com.uniflow.dto.LoginRequest;
import com.uniflow.model.User;
import com.uniflow.service.AuthService;
import com.uniflow.util.SessionUtil;
import com.uniflow.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequest loginRequest,
                                                      HttpServletRequest request,
                                                      HttpServletResponse response) {
        User user = authService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());
        
        if (user == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Invalid credentials");
            return ResponseEntity.status(401).body(error);
        }
        
        // Create session
        SessionUtil.createSession(request, user);
        
        // Create session cookie
        CookieUtil.createSessionCookie(response, request.getSession().getId());
        
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("success", true);
        responseData.put("message", "Login successful");
        responseData.put("user", Map.of(
            "id", user.getId(),
            "name", user.getName(),
            "email", user.getEmail(),
            "role", user.getRole(),
            "department", user.getDepartment()
        ));
        responseData.put("sessionId", request.getSession().getId());
        
        return ResponseEntity.ok(responseData);
    }
    
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request, HttpServletResponse response) {
        SessionUtil.invalidateSession(request);
        CookieUtil.clearAllCookies(request, response);
        
        Map<String, String> responseData = new HashMap<>();
        responseData.put("success", "true");
        responseData.put("message", "Logout successful");
        
        return ResponseEntity.ok(responseData);
    }
    
    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody User user) {
        User registeredUser = authService.register(user);
        return ResponseEntity.ok(registeredUser);
    }
    
    @GetMapping("/check-session")
    public ResponseEntity<Map<String, Object>> checkSession(HttpServletRequest request) {
        boolean isValid = SessionUtil.isSessionValid(request);
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("valid", isValid);
        
        if (isValid) {
            User user = SessionUtil.getCurrentUser(request);
            responseData.put("user", Map.of(
                "name", user.getName(),
                "email", user.getEmail(),
                "role", user.getRole()
            ));
        }
        
        return ResponseEntity.ok(responseData);
    }
}