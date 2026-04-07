package com.uniflow.controller;

import com.uniflow.model.Venue;
import com.uniflow.service.VenueService;
import com.uniflow.service.RealtimeService;
import com.uniflow.dto.RealtimeMessage;
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
    
    @Autowired
    private RealtimeService realtimeService;
    
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
        Venue savedVenue = venueService.createVenue(venue);
        realtimeService.broadcastVenueChange(RealtimeMessage.OperationType.CREATE, savedVenue);
        return ResponseEntity.ok(savedVenue);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Venue> updateVenue(@PathVariable Long id, @RequestBody Venue venue) {
        Venue updatedVenue = venueService.updateVenue(id, venue);
        realtimeService.broadcastVenueChange(RealtimeMessage.OperationType.UPDATE, updatedVenue);
        return ResponseEntity.ok(updatedVenue);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVenue(@PathVariable Long id) {
        Venue venue = venueService.getVenueById(id).orElse(null);
        if (venue != null) {
            venueService.deleteVenue(id);
            realtimeService.broadcastVenueChange(RealtimeMessage.OperationType.DELETE, venue);
        }
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