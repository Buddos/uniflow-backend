package com.uniflow.repository;

import com.uniflow.model.CourseUnitRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<CourseUnitRequest, Long> {
    List<CourseUnitRequest> findByProvidingDepartment(String department);
    List<CourseUnitRequest> findByRequestingDepartment(String department);
    List<CourseUnitRequest> findByStatus(String status);
    List<CourseUnitRequest> findByProvidingDepartmentAndStatus(String department, String status);
    
    @Query("SELECT r FROM CourseUnitRequest r WHERE r.status = 'PENDING' AND r.providingDepartment = :department")
    List<CourseUnitRequest> findPendingRequestsForDepartment(@Param("department") String department);
    
    @Query("SELECT COUNT(r) FROM CourseUnitRequest r WHERE r.status = 'PENDING'")
    long countPendingRequests();
}