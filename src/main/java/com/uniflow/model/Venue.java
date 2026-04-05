package com.uniflow.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "venues")
public class Venue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String name;
    
    private Integer capacity;
    
    private String building;
    
    private Integer floor;
    
    private String equipmentHome;
    
    private Boolean hasProjector = false;
    
    private Boolean hasWhiteboard = true;
    
    private Boolean hasAC = false;
    
    private String status = "AVAILABLE";
    
    private Double latitude;
    
    private Double longitude;
    
    private String location;
    
    @ElementCollection
    @CollectionTable(name = "venue_equipment", joinColumns = @JoinColumn(name = "venue_id"))
    @Column(name = "equipment_name")
    private List<String> equipment = new ArrayList<>();
    
    private String resourceHome;
    
    private String description;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "venue")
    private List<TimetableEntry> timetableEntries = new ArrayList<>();
    
    @OneToMany(mappedBy = "venue")
    private List<Booking> bookings = new ArrayList<>();
    
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
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
    
    public String getBuilding() { return building; }
    public void setBuilding(String building) { this.building = building; }
    
    public Integer getFloor() { return floor; }
    public void setFloor(Integer floor) { this.floor = floor; }
    
    public String getEquipmentHome() { return equipmentHome; }
    public void setEquipmentHome(String equipmentHome) { this.equipmentHome = equipmentHome; }
    
    public Boolean getHasProjector() { return hasProjector; }
    public void setHasProjector(Boolean hasProjector) { this.hasProjector = hasProjector; }
    
    public Boolean getHasWhiteboard() { return hasWhiteboard; }
    public void setHasWhiteboard(Boolean hasWhiteboard) { this.hasWhiteboard = hasWhiteboard; }
    
    public Boolean getHasAC() { return hasAC; }
    public void setHasAC(Boolean hasAC) { this.hasAC = hasAC; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public List<String> getEquipment() { return equipment; }
    public void setEquipment(List<String> equipment) { this.equipment = equipment; }
    
    public String getResourceHome() { return resourceHome; }
    public void setResourceHome(String resourceHome) { this.resourceHome = resourceHome; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public List<TimetableEntry> getTimetableEntries() { return timetableEntries; }
    public void setTimetableEntries(List<TimetableEntry> timetableEntries) { this.timetableEntries = timetableEntries; }
    
    public List<Booking> getBookings() { return bookings; }
    public void setBookings(List<Booking> bookings) { this.bookings = bookings; }
}