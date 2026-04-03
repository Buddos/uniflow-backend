package com.uniflow.service;

import com.uniflow.model.AcademicTrip;
import com.uniflow.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TripService {
    
    @Autowired
    private TripRepository tripRepository;
    
    @Autowired
    private TimetableService timetableService;
    
    public AcademicTrip createTrip(AcademicTrip trip) {
        AcademicTrip savedTrip = tripRepository.save(trip);
        
        // Auto-release venues for the affected cohort
        timetableService.autoReleaseVenuesForTrip(
            trip.getCohort(),
            trip.getStartDate().toString(),
            trip.getEndDate().toString()
        );
        
        return savedTrip;
    }
    
    public List<AcademicTrip> getAllTrips() {
        return tripRepository.findAll();
    }
    
    public AcademicTrip getTripById(Long id) {
        return tripRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Trip not found"));
    }
    
    public List<AcademicTrip> getTripsByDepartment(String department) {
        return tripRepository.findByDepartment(department);
    }
    
    public List<AcademicTrip> getTripsByCohort(String cohort) {
        return tripRepository.findByCohort(cohort);
    }
    
    public AcademicTrip updateTripStatus(Long id, String status) {
        AcademicTrip trip = getTripById(id);
        trip.setStatus(status);
        return tripRepository.save(trip);
    }
    
    public void deleteTrip(Long id) {
        tripRepository.deleteById(id);
    }
    
    public List<AcademicTrip> getUpcomingTrips() {
        return tripRepository.findUpcomingTrips();
    }
    
    public List<AcademicTrip> getActiveTrips() {
        return tripRepository.findActiveTripsOnDate(LocalDate.now());
    }
    
    public Map<String, Object> getTripStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("upcoming", getUpcomingTrips().size());
        stats.put("active", getActiveTrips().size());
        stats.put("total", tripRepository.count());
        stats.put("completed", tripRepository.findByStatus("COMPLETED").size());
        return stats;
    }
}