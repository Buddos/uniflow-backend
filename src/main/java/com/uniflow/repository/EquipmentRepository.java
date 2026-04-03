package com.uniflow.repository;

import com.uniflow.model.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    List<Equipment> findByHomeDepartment(String department);
    List<Equipment> findByCurrentVenue(String venue);
    List<Equipment> findByStatus(String status);
    List<Equipment> findByType(String type);
    
    @Query("SELECT e FROM Equipment e WHERE e.status = 'AVAILABLE' AND e.homeDepartment = :department")
    List<Equipment> findAvailableEquipmentByDepartment(@Param("department") String department);
    
    @Query("SELECT COUNT(e) FROM Equipment e WHERE e.status = 'IN_USE'")
    long countEquipmentInUse();
}