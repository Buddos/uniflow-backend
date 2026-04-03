package com.uniflow.controller;

import com.uniflow.dto.TripRequestDTO;
import com.uniflow.model.AcademicTrip;
import com.uniflow.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/trips")
@CrossOrigin(origins = "*")
public class TripController {
    
    @Autowired
    private TripService tripService;
    
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllTrips() {
        List<AcademicTrip> trips = tripService.getAllTrips();
        List<Map<String, Object>> academicTrips = trips.stream().map(trip -> {
            Map<String, Object> at = new HashMap<>();
            at.put("id", trip.getId().toString());
            at.put("cohort", trip.getCohort());
            at.put("courseUnit", trip.getTitle() != null ? trip.getTitle() : "Unknown Course");
            at.put("startDate", trip.getStartDate().toString());
            at.put("endDate", trip.getEndDate().toString());
            at.put("destination", trip.getDestination());
            at.put("affectedSlots", trip.getNumberOfStudents() != null ? trip.getNumberOfStudents() : 0);
            at.put("status", trip.getStatus() != null ? trip.getStatus().toLowerCase() : "scheduled");
            return at;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(academicTrips);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<AcademicTrip> getTripById(@PathVariable Long id) {
        return ResponseEntity.ok(tripService.getTripById(id));
    }
    
    @GetMapping("/department/{department}")
    public ResponseEntity<List<AcademicTrip>> getTripsByDepartment(@PathVariable String department) {
        return ResponseEntity.ok(tripService.getTripsByDepartment(department));
    }
    
    @GetMapping("/cohort/{cohort}")
    public ResponseEntity<List<AcademicTrip>> getTripsByCohort(@PathVariable String cohort) {
        return ResponseEntity.ok(tripService.getTripsByCohort(cohort));
    }
    
    @GetMapping("/upcoming")
    public ResponseEntity<List<AcademicTrip>> getUpcomingTrips() {
        return ResponseEntity.ok(tripService.getUpcomingTrips());
    }
    
    @PostMapping
    public ResponseEntity<AcademicTrip> createTrip(@RequestBody TripRequestDTO tripDTO) {
        AcademicTrip trip = new AcademicTrip();
        trip.setTitle(tripDTO.getTitle());
        trip.setDestination(tripDTO.getDestination());
        trip.setCohort(tripDTO.getCohort());
        trip.setStartDate(tripDTO.getStartDate());
        trip.setEndDate(tripDTO.getEndDate());
        trip.setDescription(tripDTO.getDescription());
        trip.setDepartment(tripDTO.getDepartment());
        
        return ResponseEntity.ok(tripService.createTrip(trip));
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<AcademicTrip> updateTripStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return ResponseEntity.ok(tripService.updateTripStatus(id, status));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTrip(@PathVariable Long id) {
        tripService.deleteTrip(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        return ResponseEntity.ok(tripService.getTripStatistics());
    }
}