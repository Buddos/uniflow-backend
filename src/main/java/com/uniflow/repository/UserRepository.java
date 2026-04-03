package com.uniflow.repository;

import com.uniflow.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByRole(String role);
    List<User> findByDepartment(String department);
    List<User> findByIsActive(Boolean isActive);
    boolean existsByEmail(String email);
    
    @Query("SELECT u FROM User u WHERE u.role = 'LECTURER' AND u.department = :department")
    List<User> findLecturersByDepartment(@Param("department") String department);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.lastLogin > :date")
    long countActiveUsersSince(@Param("date") java.time.LocalDateTime date);
}