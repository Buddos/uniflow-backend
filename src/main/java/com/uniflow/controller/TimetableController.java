package com.uniflow.controller;

import com.uniflow.model.TimetableEntry;
import com.uniflow.service.TimetableService;
import com.uniflow.service.RealtimeService;
import com.uniflow.dto.RealtimeMessage;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/timetable")
public class TimetableController {
    
    @Autowired
    private TimetableService timetableService;
    
    @Autowired
    private RealtimeService realtimeService;
    
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllTimetable() {
        List<TimetableEntry> entries = timetableService.getAllTimetableEntries();
        List<Map<String, Object>> slots = entries.stream().map(entry -> {
            Map<String, Object> slot = new HashMap<>();
            slot.put("id", entry.getId().toString());
            slot.put("day", entry.getDayOfWeek());
            
            // Format time slot as "HH:MM - HH:MM"
            String timeSlot = entry.getStartTime() != null && entry.getEndTime() != null
                ? entry.getStartTime().toString().substring(0, 5) + " - " + entry.getEndTime().toString().substring(0, 5)
                : "TBD";
            slot.put("timeSlot", timeSlot);
            
            // Flatten course unit - return name and code as strings
            slot.put("courseUnit", entry.getCourseUnit() != null ? entry.getCourseUnit().getName() : "Unknown");
            slot.put("courseCode", entry.getCourseUnit() != null ? entry.getCourseUnit().getCode() : "");
            
            // Flatten venue - return name as string
            slot.put("venue", entry.getVenue() != null ? entry.getVenue().getName() : "Unknown");
            
            // Cohort size should come from admitted cohort projections
            slot.put("cohortSize", entry.getTotalAdmittedStudents() != null ? entry.getTotalAdmittedStudents() : 0);
            
            // Keep lecturer and department as strings
            slot.put("lecturer", entry.getLecturer() != null ? entry.getLecturer().getName() : "Unknown");
            slot.put("department", entry.getLecturer() != null ? entry.getLecturer().getDepartment() : "Unknown");
            slot.put("color", entry.getColorCode() != null ? entry.getColorCode() : "#3b82f6");
            return slot;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(slots);
    }
    
    @GetMapping("/lecturer/{lecturerId}")
    public ResponseEntity<List<TimetableEntry>> getLecturerTimetable(@PathVariable Long lecturerId) {
        return ResponseEntity.ok(timetableService.getLecturerTimetable(lecturerId));
    }
    
    @GetMapping("/venue/{venueId}")
    public ResponseEntity<List<TimetableEntry>> getVenueTimetable(@PathVariable Long venueId) {
        return ResponseEntity.ok(timetableService.getVenueTimetable(venueId));
    }
    
    @GetMapping("/cohort/{cohort}")
    public ResponseEntity<List<TimetableEntry>> getCohortTimetable(
            @PathVariable String cohort,
            @RequestParam String academicYear,
            @RequestParam String semester) {
        return ResponseEntity.ok(timetableService.getCohortTimetable(cohort, academicYear, semester));
    }
    
    @PostMapping
    public ResponseEntity<TimetableEntry> createTimetableEntry(@Valid @RequestBody TimetableEntry entry) {
        TimetableEntry savedEntry = timetableService.createTimetableEntry(entry);
        realtimeService.broadcastTimetableChange(RealtimeMessage.OperationType.CREATE, savedEntry);
        return ResponseEntity.ok(savedEntry);
    }
    
    @GetMapping("/conflicts")
    public ResponseEntity<Map<String, Object>> getConflicts() {
        return ResponseEntity.ok(timetableService.getTimetableConflicts());
    }
}