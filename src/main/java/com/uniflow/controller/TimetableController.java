package com.uniflow.controller;

import com.uniflow.model.TimetableEntry;
import com.uniflow.service.TimetableService;
import com.uniflow.service.RealtimeService;
import com.uniflow.dto.RealtimeMessage;
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
@CrossOrigin(origins = "*")
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
            slot.put("startTime", entry.getStartTime().toString());
            slot.put("endTime", entry.getEndTime().toString());
            slot.put("courseUnit", Map.of(
                "id", entry.getCourseUnit().getId().toString(),
                "code", entry.getCourseUnit().getCode(),
                "name", entry.getCourseUnit().getName(),
                "department", entry.getCourseUnit().getDepartment(),
                "creditHours", entry.getCourseUnit().getCreditHours()
            ));
            slot.put("venue", Map.of(
                "id", entry.getVenue().getId().toString(),
                "name", entry.getVenue().getName(),
                "capacity", entry.getVenue().getCapacity(),
                "location", entry.getVenue().getLocation() != null ? entry.getVenue().getLocation() : "Main Campus",
                "building", entry.getVenue().getBuilding(),
                "equipment", entry.getVenue().getEquipment() != null ? entry.getVenue().getEquipment() : new ArrayList<String>(),
                "resourceHome", entry.getVenue().getResourceHome() != null ? entry.getVenue().getResourceHome() : entry.getVenue().getEquipmentHome(),
                "status", entry.getVenue().getStatus() != null ? entry.getVenue().getStatus().toLowerCase() : "available"
            ));
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
    public ResponseEntity<TimetableEntry> createTimetableEntry(@RequestBody TimetableEntry entry) {
        TimetableEntry savedEntry = timetableService.createTimetableEntry(entry);
        realtimeService.broadcastTimetableChange(RealtimeMessage.OperationType.CREATE, savedEntry);
        return ResponseEntity.ok(savedEntry);
    }
    
    @GetMapping("/conflicts")
    public ResponseEntity<Map<String, Object>> getConflicts() {
        return ResponseEntity.ok(timetableService.getTimetableConflicts());
    }
}