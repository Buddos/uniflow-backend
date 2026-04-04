package com.uniflow.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "course_unit_requests")
public class CourseUnitRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "course_unit_id")
    private CourseUnit courseUnit;
    
    private String requestingDepartment;
    
    private String providingDepartment;
    
    private Integer expectedStudents;
    
    private String status = "PENDING"; // PENDING, ACCEPTED, REJECTED
    
    private String rejectionReason;
    
    private String comments;
    
    private LocalDateTime requestedAt;
    
    private LocalDateTime respondedAt;
    
    @PrePersist
    protected void onCreate() {
        requestedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public CourseUnit getCourseUnit() { return courseUnit; }
    public void setCourseUnit(CourseUnit courseUnit) { this.courseUnit = courseUnit; }
    
    public String getRequestingDepartment() { return requestingDepartment; }
    public void setRequestingDepartment(String requestingDepartment) { this.requestingDepartment = requestingDepartment; }
    
    public String getProvidingDepartment() { return providingDepartment; }
    public void setProvidingDepartment(String providingDepartment) { this.providingDepartment = providingDepartment; }
    
    public Integer getExpectedStudents() { return expectedStudents; }
    public void setExpectedStudents(Integer expectedStudents) { this.expectedStudents = expectedStudents; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
    
    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
    
    public LocalDateTime getRequestedAt() { return requestedAt; }
    public void setRequestedAt(LocalDateTime requestedAt) { this.requestedAt = requestedAt; }
    
    public LocalDateTime getRespondedAt() { return respondedAt; }
    public void setRespondedAt(LocalDateTime respondedAt) { this.respondedAt = respondedAt; }
}