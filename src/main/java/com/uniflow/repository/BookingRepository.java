package com.uniflow.repository;

import com.uniflow.model.Booking;
import com.uniflow.model.User;
import com.uniflow.model.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookedBy(User user);
    List<Booking> findByVenue(Venue venue);
    List<Booking> findByStatus(String status);
    List<Booking> findByBookingType(String bookingType);
    
    @Query("SELECT b FROM Booking b WHERE b.venue = :venue AND b.startTime < :endTime AND b.endTime > :startTime AND b.status = 'CONFIRMED'")
    List<Booking> findConflictingBookings(@Param("venue") Venue venue,
                                          @Param("startTime") LocalDateTime startTime,
                                          @Param("endTime") LocalDateTime endTime);
    
    @Query("SELECT b FROM Booking b WHERE b.bookedBy.id = :userId AND b.startTime >= :startDate ORDER BY b.startTime DESC")
    List<Booking> findUserBookingsAfterDate(@Param("userId") Long userId, @Param("startDate") LocalDateTime startDate);
    
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.startTime >= :startDate AND b.status = 'CONFIRMED'")
    long countBookingsAfterDate(@Param("startDate") LocalDateTime startDate);

    boolean existsByVenueAndBookingDateAndStartTime(Venue venue, LocalDate bookingDate, LocalDateTime startTime);
}