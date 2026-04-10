package com.uniflow.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "timetable_entries")
public class TimetableEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "venue_id")
    @NotNull(message = "Venue is required")
    private Venue venue;
    
    @ManyToOne
    @JoinColumn(name = "course_unit_id")
    private CourseUnit courseUnit;
    
    @ManyToOne
    @JoinColumn(name = "lecturer_id")
    private User lecturer;
    
    private String dayOfWeek; // MONDAY, TUESDAY, etc.
    
    @NotNull(message = "Start time is required")
    private LocalTime startTime;
    
    @NotNull(message = "End time is required")
    private LocalTime endTime;
    
    private String academicYear;
    
    private String semester;
    
    private String cohort;
    
    @NotNull(message = "Total admitted students is required")
    @Min(value = 1, message = "Total admitted students must be at least 1")
    private Integer totalAdmittedStudents;

    private Integer registeredStudents;

    private Integer expectedStudents;
    
    private Boolean isMakeupClass = false;
    
    private String status = "SCHEDULED"; // SCHEDULED, CANCELLED, RESCHEDULED
    
    private String colorCode;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Venue getVenue() { return venue; }
    public void setVenue(Venue venue) { this.venue = venue; }
    
    public CourseUnit getCourseUnit() { return courseUnit; }
    public void setCourseUnit(CourseUnit courseUnit) { this.courseUnit = courseUnit; }
    
    public User getLecturer() { return lecturer; }
    public void setLecturer(User lecturer) { this.lecturer = lecturer; }
    
    public String getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    
    public String getAcademicYear() { return academicYear; }
    public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }
    
    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }
    
    public String getCohort() { return cohort; }
    public void setCohort(String cohort) { this.cohort = cohort; }

    public Integer getTotalAdmittedStudents() { return totalAdmittedStudents; }
    public void setTotalAdmittedStudents(Integer totalAdmittedStudents) { this.totalAdmittedStudents = totalAdmittedStudents; }

    public Integer getRegisteredStudents() { return registeredStudents; }
    public void setRegisteredStudents(Integer registeredStudents) { this.registeredStudents = registeredStudents; }
    
    public Integer getExpectedStudents() { return expectedStudents; }
    public void setExpectedStudents(Integer expectedStudents) { this.expectedStudents = expectedStudents; }
    
    public Boolean getIsMakeupClass() { return isMakeupClass; }
    public void setIsMakeupClass(Boolean isMakeupClass) { this.isMakeupClass = isMakeupClass; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getColorCode() { return colorCode; }
    public void setColorCode(String colorCode) { this.colorCode = colorCode; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}