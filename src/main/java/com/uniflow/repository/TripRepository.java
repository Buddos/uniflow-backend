package com.uniflow.repository;

import com.uniflow.model.AcademicTrip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<AcademicTrip, Long> {
    List<AcademicTrip> findByDepartment(String department);
    List<AcademicTrip> findByCohort(String cohort);
    List<AcademicTrip> findByStartDateBetween(LocalDate startDate, LocalDate endDate);
    List<AcademicTrip> findByStatus(String status);
    
    @Query("SELECT t FROM AcademicTrip t WHERE t.startDate >= :startDate AND t.startDate <= :endDate")
    List<AcademicTrip> findTripsInDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT t FROM AcademicTrip t WHERE t.startDate <= :date AND t.endDate >= :date")
    List<AcademicTrip> findActiveTripsOnDate(@Param("date") LocalDate date);
    
    @Query("SELECT t FROM AcademicTrip t WHERE t.startDate >= CURRENT_DATE ORDER BY t.startDate ASC")
    List<AcademicTrip> findUpcomingTrips();
}