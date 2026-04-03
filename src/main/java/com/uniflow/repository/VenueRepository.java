package com.uniflow.repository;

import com.uniflow.model.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Long> {
    Optional<Venue> findByName(String name);
    List<Venue> findByStatus(String status);
    List<Venue> findByCapacityGreaterThanEqual(Integer capacity);
    List<Venue> findByBuilding(String building);
    List<Venue> findByHasProjector(Boolean hasProjector);
    
    @Query("SELECT v FROM Venue v WHERE v.capacity >= :requiredCapacity AND v.status = 'AVAILABLE'")
    List<Venue> findAvailableVenuesWithCapacity(@Param("requiredCapacity") Integer requiredCapacity);
    
    @Query("SELECT COUNT(v) FROM Venue v WHERE v.status = 'AVAILABLE'")
    Long countAvailableVenues();
    
    @Query("SELECT COUNT(v) FROM Venue v WHERE v.status = 'BOOKED'")
    Long countBookedVenues();
    
    @Query("SELECT v.building, COUNT(v) FROM Venue v GROUP BY v.building")
    List<Object[]> countVenuesByBuilding();
}