package com.uniflow.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Table(
    name = "bookings",
    uniqueConstraints = @UniqueConstraint(columnNames = {"venue_id", "booking_date", "start_time"})
)
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "venue_id")
    private Venue venue;
    
    @ManyToOne
    @JoinColumn(name = "timetable_entry_id")
    private TimetableEntry timetableEntry;
    
    @ManyToOne
    @JoinColumn(name = "booked_by_id")
    private User bookedBy;
    
    private LocalDateTime startTime;

    @Column(name = "booking_date", nullable = false)
    private LocalDate bookingDate;
    
    private LocalDateTime endTime;
    
    private String purpose;
    
    private String status = "CONFIRMED"; // CONFIRMED, CANCELLED, COMPLETED
    
    private String bookingType = "REGULAR"; // REGULAR, MAKEUP, EVENT
    
    private String notes;
    
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
    
    public TimetableEntry getTimetableEntry() { return timetableEntry; }
    public void setTimetableEntry(TimetableEntry timetableEntry) { this.timetableEntry = timetableEntry; }
    
    public User getBookedBy() { return bookedBy; }
    public void setBookedBy(User bookedBy) { this.bookedBy = bookedBy; }
    
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDate getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDate bookingDate) { this.bookingDate = bookingDate; }
    
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    
    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getBookingType() { return bookingType; }
    public void setBookingType(String bookingType) { this.bookingType = bookingType; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}