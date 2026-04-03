package com.uniflow.service;

import com.uniflow.model.TimetableEntry;
import com.uniflow.model.Venue;
import com.uniflow.model.User;
import com.uniflow.repository.TimetableRepository;
import com.uniflow.repository.VenueRepository;
import com.uniflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TimetableService {
    
    @Autowired
    private TimetableRepository timetableRepository;
    
    @Autowired
    private VenueRepository venueRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public List<TimetableEntry> getAllTimetableEntries() {
        return timetableRepository.findAll();
    }
    
    public TimetableEntry createTimetableEntry(TimetableEntry entry) {
        // Apply 110% capacity rule
        if (entry.getExpectedStudents() != null) {
            int requiredCapacity = (int) Math.ceil(entry.getExpectedStudents() * 1.1);
            Venue venue = entry.getVenue();
            if (venue.getCapacity() < requiredCapacity) {
                throw new RuntimeException("Venue capacity insufficient. Required: " + requiredCapacity + ", Available: " + venue.getCapacity());
            }
        }
        
        // Assign color based on department
        if (entry.getCourseUnit() != null) {
            String department = entry.getCourseUnit().getDepartment();
            entry.setColorCode(getDepartmentColor(department));
        }
        
        return timetableRepository.save(entry);
    }
    
    public List<TimetableEntry> getLecturerTimetable(Long lecturerId) {
        User lecturer = userRepository.findById(lecturerId)
            .orElseThrow(() -> new RuntimeException("Lecturer not found"));
        return timetableRepository.findByLecturer(lecturer);
    }
    
    public List<TimetableEntry> getVenueTimetable(Long venueId) {
        Venue venue = venueRepository.findById(venueId)
            .orElseThrow(() -> new RuntimeException("Venue not found"));
        return timetableRepository.findByVenue(venue);
    }
    
    public List<TimetableEntry> getCohortTimetable(String cohort, String academicYear, String semester) {
        return timetableRepository.findByCohortAndAcademicYearAndSemester(cohort, academicYear, semester);
    }
    
    public List<TimetableEntry> getDaySchedule(String dayOfWeek) {
        return timetableRepository.findByDayOfWeekAndCohort(dayOfWeek, null);
    }
    
    public void autoReleaseVenuesForTrip(String cohort, String startDate, String endDate) {
        List<TimetableEntry> entries = timetableRepository.findByCohortAndAcademicYearAndSemester(cohort, "2024", "1");
        for (TimetableEntry entry : entries) {
            entry.setStatus("CANCELLED");
            timetableRepository.save(entry);
            
            Venue venue = entry.getVenue();
            venue.setStatus("AVAILABLE");
            venueRepository.save(venue);
        }
    }
    
    public Map<String, Object> getTimetableConflicts() {
        Map<String, Object> conflicts = new HashMap<>();
        List<TimetableEntry> allEntries = timetableRepository.findAll();
        
        // Check for venue double-booking
        Map<String, List<TimetableEntry>> venueSchedule = new HashMap<>();
        List<String> venueConflicts = new java.util.ArrayList<>();
        
        for (TimetableEntry entry : allEntries) {
            String key = entry.getVenue().getId() + "-" + entry.getDayOfWeek() + "-" + entry.getStartTime();
            if (venueSchedule.containsKey(key)) {
                venueConflicts.add("Venue " + entry.getVenue().getName() + " has conflict on " + entry.getDayOfWeek());
            } else {
                venueSchedule.put(key, new java.util.ArrayList<>());
            }
            venueSchedule.get(key).add(entry);
        }
        
        conflicts.put("venueConflicts", venueConflicts);
        conflicts.put("totalEntries", allEntries.size());
        
        return conflicts;
    }
    
    private String getDepartmentColor(String department) {
        Map<String, String> colors = new HashMap<>();
        colors.put("Computer Science", "#3B82F6");
        colors.put("Mathematics", "#10B981");
        colors.put("Physics", "#F59E0B");
        colors.put("Engineering", "#EF4444");
        colors.put("Business", "#8B5CF6");
        return colors.getOrDefault(department, "#6B7280");
    }
}