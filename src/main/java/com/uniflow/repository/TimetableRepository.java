package com.uniflow.repository;

import com.uniflow.model.TimetableEntry;
import com.uniflow.model.Venue;
import com.uniflow.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface TimetableRepository extends JpaRepository<TimetableEntry, Long> {
    List<TimetableEntry> findByLecturer(User lecturer);
    List<TimetableEntry> findByVenue(Venue venue);
    List<TimetableEntry> findByDayOfWeekAndCohort(String dayOfWeek, String cohort);
    List<TimetableEntry> findByAcademicYearAndSemester(String academicYear, String semester);
    List<TimetableEntry> findByIsMakeupClass(Boolean isMakeupClass);
    
    @Query("SELECT t FROM TimetableEntry t WHERE t.dayOfWeek = :dayOfWeek AND t.startTime >= :startTime AND t.endTime <= :endTime")
    List<TimetableEntry> findByDayAndTimeRange(@Param("dayOfWeek") String dayOfWeek, 
                                                @Param("startTime") LocalTime startTime, 
                                                @Param("endTime") LocalTime endTime);
    
    @Query("SELECT t FROM TimetableEntry t WHERE t.cohort = :cohort AND t.academicYear = :academicYear AND t.semester = :semester")
    List<TimetableEntry> findByCohortAndAcademicYearAndSemester(@Param("cohort") String cohort,
                                                                 @Param("academicYear") String academicYear,
                                                                 @Param("semester") String semester);
    
    @Query("SELECT t FROM TimetableEntry t WHERE t.lecturer.id = :lecturerId AND t.dayOfWeek = :dayOfWeek")
    List<TimetableEntry> findLecturerScheduleForDay(@Param("lecturerId") Long lecturerId, @Param("dayOfWeek") String dayOfWeek);
}