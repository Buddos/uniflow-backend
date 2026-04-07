package com.uniflow.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "class_rep_feedback")
public class ClassRepFeedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String courseCode;
    private String courseUnit;
    private String venue;
    private String issue; // overcrowding, equipment, wrong-venue, scheduling-conflict, other
    private String description;
    private String reportedBy;
    private LocalDateTime reportedDate;
    private String status; // open, resolved, dismissed
    private String priority; // low, medium, high, urgent

    @PrePersist
    protected void onCreate() {
        reportedDate = LocalDateTime.now();
        if (status == null) {
            status = "open";
        }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getCourseUnit() { return courseUnit; }
    public void setCourseUnit(String courseUnit) { this.courseUnit = courseUnit; }

    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }

    public String getIssue() { return issue; }
    public void setIssue(String issue) { this.issue = issue; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getReportedBy() { return reportedBy; }
    public void setReportedBy(String reportedBy) { this.reportedBy = reportedBy; }

    public LocalDateTime getReportedDate() { return reportedDate; }
    public void setReportedDate(LocalDateTime reportedDate) { this.reportedDate = reportedDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
}