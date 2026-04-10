package com.uniflow.service;

import com.uniflow.model.Venue;
import com.uniflow.repository.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class VenueService {
    
    @Autowired
    private VenueRepository venueRepository;
    
    public List<Venue> getAllVenues() {
        return venueRepository.findAll();
    }
    
    public Optional<Venue> getVenueById(Long id) {
        return venueRepository.findById(id);
    }
    
    public Venue createVenue(Venue venue) {
        // Map fields for frontend compatibility
        mapVenueFields(venue);
        return venueRepository.save(venue);
    }
    
    public Venue updateVenue(Long id, Venue venueDetails) {
        Venue venue = venueRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Venue not found"));
        
        venue.setName(venueDetails.getName());
        venue.setCapacity(venueDetails.getCapacity());
        venue.setBuilding(venueDetails.getBuilding());
        venue.setFloor(venueDetails.getFloor());
        venue.setEquipmentHome(venueDetails.getEquipmentHome());
        venue.setEquipmentOfficeName(venueDetails.getEquipmentOfficeName());
        venue.setDistanceFromOfficeMeters(venueDetails.getDistanceFromOfficeMeters());
        venue.setHasProjector(venueDetails.getHasProjector());
        venue.setHasWhiteboard(venueDetails.getHasWhiteboard());
        venue.setHasAC(venueDetails.getHasAC());
        venue.setStatus(venueDetails.getStatus());
        venue.setDescription(venueDetails.getDescription());
        
        // Map additional fields
        mapVenueFields(venue);
        
        return venueRepository.save(venue);
    }
    
    public void deleteVenue(Long id) {
        venueRepository.deleteById(id);
    }
    
    public List<Venue> getAvailableVenues() {
        return venueRepository.findByStatus("AVAILABLE");
    }
    
    public List<Venue> getVenuesByCapacity(Integer minCapacity) {
        return venueRepository.findByCapacityGreaterThanEqual(minCapacity);
    }
    
    public long getTotalVenues() {
        return venueRepository.count();
    }
    
    public long getOccupiedVenues() {
        return venueRepository.countBookedVenues();
    }
    
    public long getAvailableVenuesCount() {
        return venueRepository.countAvailableVenues();
    }
    
    public List<Venue> getVenuesByBuilding(String building) {
        return venueRepository.findByBuilding(building);
    }
    
    public Map<String, Long> getVenueStatistics() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("total", getTotalVenues());
        stats.put("available", getAvailableVenuesCount());
        stats.put("occupied", getOccupiedVenues());
        stats.put("maintenance", (long) venueRepository.findByStatus("MAINTENANCE").size());
        return stats;
    }
    
    public List<Venue> findOptimalVenue(Integer requiredCapacity, Boolean needsProjector) {
        List<Venue> venues = venueRepository.findAvailableVenuesWithCapacity(requiredCapacity);
        
        if (needsProjector) {
            venues = venues.stream()
                .filter(Venue::getHasProjector)
                .toList();
        }

        venues = venues.stream()
            .sorted(
                Comparator
                    .comparing((Venue venue) -> !isWithinProximityLimit(venue))
                    .thenComparing(Venue::getDistanceFromOfficeMeters, Comparator.nullsLast(Integer::compareTo))
            )
            .toList();
        
        return venues;
    }

    private boolean isWithinProximityLimit(Venue venue) {
        Integer distance = venue.getDistanceFromOfficeMeters();
        return distance != null && distance <= 300;
    }
    
    private void mapVenueFields(Venue venue) {
        // Set default location if not provided
        if (venue.getLocation() == null) {
            venue.setLocation("Main Campus");
        }
        
        // Map equipment based on boolean fields
        List<String> equipment = new ArrayList<>();
        if (venue.getHasProjector() != null && venue.getHasProjector()) {
            equipment.add("Projector");
        }
        if (venue.getHasWhiteboard() != null && venue.getHasWhiteboard()) {
            equipment.add("Whiteboard");
        }
        if (venue.getHasAC() != null && venue.getHasAC()) {
            equipment.add("PA System");
        }
        venue.setEquipment(equipment);
        
        // Map resourceHome
        if (venue.getResourceHome() == null && venue.getEquipmentHome() != null) {
            venue.setResourceHome(venue.getEquipmentHome());
        }
        
        // Normalize status
        if (venue.getStatus() != null) {
            venue.setStatus(venue.getStatus().toLowerCase());
        }
    }
}