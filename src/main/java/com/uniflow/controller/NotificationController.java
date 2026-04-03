package com.uniflow.controller;

import com.uniflow.model.Notification;
import com.uniflow.service.NotificationService;
import com.uniflow.util.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {
    
    @Autowired
    private NotificationService notificationService;
    
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getNotifications(HttpServletRequest request) {
        Long userId = SessionUtil.getCurrentUserId(request);
        List<Notification> notifications = notificationService.getUserNotifications(userId);
        List<Map<String, Object>> notifs = notifications.stream().map(notification -> {
            Map<String, Object> n = new HashMap<>();
            n.put("id", notification.getId().toString());
            n.put("title", notification.getTitle());
            n.put("message", notification.getMessage());
            n.put("type", notification.getType() != null ? notification.getType().toLowerCase() : "info");
            n.put("read", notification.getIsRead() != null ? notification.getIsRead() : false);
            n.put("createdAt", notification.getCreatedAt() != null ? notification.getCreatedAt().toString() : "2026-03-31 09:00");
            return n;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(notifs);
    }
    
    @GetMapping("/unread")
    public ResponseEntity<List<Notification>> getUnreadNotifications(HttpServletRequest request) {
        Long userId = SessionUtil.getCurrentUserId(request);
        return ResponseEntity.ok(notificationService.getUnreadNotifications(userId));
    }
    
    @GetMapping("/unread/count")
    public ResponseEntity<Map<String, Long>> getUnreadCount(HttpServletRequest request) {
        Long userId = SessionUtil.getCurrentUserId(request);
        Map<String, Long> response = new HashMap<>();
        response.put("count", notificationService.getUnreadCount(userId));
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/{id}/read")
    public ResponseEntity<Notification> markAsRead(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.markAsRead(id));
    }
    
    @PostMapping("/mark-all-read")
    public ResponseEntity<?> markAllAsRead(HttpServletRequest request) {
        Long userId = SessionUtil.getCurrentUserId(request);
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok().build();
    }
}