package com.uniflow.controller;

import com.uniflow.model.ClassRepFeedback;
import com.uniflow.service.ClassRepFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/class-rep-feedback")
@CrossOrigin(origins = "*")
public class ClassRepFeedbackController {

    @Autowired
    private ClassRepFeedbackService feedbackService;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllFeedback() {
        List<ClassRepFeedback> feedback = feedbackService.getAllFeedback();
        List<Map<String, Object>> feedbackList = feedback.stream().map(f -> {
            Map<String, Object> item = new HashMap<>();
            item.put("id", f.getId().toString());
            item.put("courseCode", f.getCourseCode());
            item.put("courseUnit", f.getCourseUnit());
            item.put("venue", f.getVenue());
            item.put("issue", f.getIssue());
            item.put("description", f.getDescription());
            item.put("reportedBy", f.getReportedBy());
            item.put("reportedDate", f.getReportedDate() != null ? f.getReportedDate().toString() : null);
            item.put("status", f.getStatus());
            item.put("priority", f.getPriority() != null ? f.getPriority() : "medium");
            return item;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(feedbackList);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> submitFeedback(@RequestBody Map<String, Object> feedbackData) {
        ClassRepFeedback feedback = new ClassRepFeedback();
        feedback.setCourseCode((String) feedbackData.get("courseCode"));
        feedback.setCourseUnit((String) feedbackData.get("courseUnit"));
        feedback.setVenue((String) feedbackData.get("venue"));
        feedback.setIssue((String) feedbackData.get("issue"));
        feedback.setDescription((String) feedbackData.get("description"));
        feedback.setReportedBy((String) feedbackData.get("reportedBy"));
        feedback.setStatus("open");
        feedback.setPriority((String) feedbackData.getOrDefault("priority", "medium"));

        ClassRepFeedback saved = feedbackService.submitFeedback(feedback);

        Map<String, Object> response = new HashMap<>();
        response.put("id", saved.getId().toString());
        response.put("courseCode", saved.getCourseCode());
        response.put("courseUnit", saved.getCourseUnit());
        response.put("venue", saved.getVenue());
        response.put("issue", saved.getIssue());
        response.put("description", saved.getDescription());
        response.put("reportedBy", saved.getReportedBy());
        response.put("reportedDate", saved.getReportedDate().toString());
        response.put("status", saved.getStatus());
        response.put("priority", saved.getPriority() != null ? saved.getPriority() : "medium");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getFeedbackById(@PathVariable Long id) {
        ClassRepFeedback feedback = feedbackService.getFeedbackById(id);
        Map<String, Object> item = new HashMap<>();
        item.put("id", feedback.getId().toString());
        item.put("courseCode", feedback.getCourseCode());
        item.put("courseUnit", feedback.getCourseUnit());
        item.put("venue", feedback.getVenue());
        item.put("issue", feedback.getIssue());
        item.put("description", feedback.getDescription());
        item.put("reportedBy", feedback.getReportedBy());
        item.put("reportedDate", feedback.getReportedDate().toString());
        item.put("status", feedback.getStatus());
        item.put("priority", feedback.getPriority() != null ? feedback.getPriority() : "medium");
        return ResponseEntity.ok(item);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Map<String, Object>> updateFeedbackStatus(@PathVariable Long id, @RequestBody Map<String, String> statusData) {
        String status = statusData.get("status");
        ClassRepFeedback updated = feedbackService.updateFeedbackStatus(id, status);
        Map<String, Object> item = new HashMap<>();
        item.put("id", updated.getId().toString());
        item.put("courseCode", updated.getCourseCode());
        item.put("courseUnit", updated.getCourseUnit());
        item.put("venue", updated.getVenue());
        item.put("issue", updated.getIssue());
        item.put("description", updated.getDescription());
        item.put("reportedBy", updated.getReportedBy());
        item.put("reportedDate", updated.getReportedDate().toString());
        item.put("status", updated.getStatus());
        item.put("priority", updated.getPriority() != null ? updated.getPriority() : "medium");
        return ResponseEntity.ok(item);
    }
}