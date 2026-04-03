package com.uniflow.controller;

import com.uniflow.model.Venue;
import com.uniflow.service.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/venues")
@CrossOrigin(origins = "*")
public class VenueController {
    
    @Autowired
    private VenueService venueService;
    
    @GetMapping
    public ResponseEntity<List<Venue>> getAllVenues() {
        return ResponseEntity.ok(venueService.getAllVenues());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Venue> getVenueById(@PathVariable Long id) {
        return venueService.getVenueById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Venue> createVenue(@RequestBody Venue venue) {
        return ResponseEntity.ok(venueService.createVenue(venue));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Venue> updateVenue(@PathVariable Long id, @RequestBody Venue venue) {
        return ResponseEntity.ok(venueService.updateVenue(id, venue));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVenue(@PathVariable Long id) {
        venueService.deleteVenue(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/available")
    public ResponseEntity<List<Venue>> getAvailableVenues() {
        return ResponseEntity.ok(venueService.getAvailableVenues());
    }
    
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getVenueStats() {
        return ResponseEntity.ok(venueService.getVenueStatistics());
    }
    
    @GetMapping("/building/{building}")
    public ResponseEntity<List<Venue>> getVenuesByBuilding(@PathVariable String building) {
        return ResponseEntity.ok(venueService.getVenuesByBuilding(building));
    }
    
    @GetMapping("/optimal")
    public ResponseEntity<List<Venue>> findOptimalVenues(
            @RequestParam Integer capacity,
            @RequestParam(required = false) Boolean projector) {
        return ResponseEntity.ok(venueService.findOptimalVenue(capacity, projector != null ? projector : false));
    }
}