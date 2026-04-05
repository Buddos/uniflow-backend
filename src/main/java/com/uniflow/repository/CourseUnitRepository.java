package com.uniflow.repository;

import com.uniflow.model.CourseUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CourseUnitRepository extends JpaRepository<CourseUnit, Long> {
    Optional<CourseUnit> findByCode(String code);
    List<CourseUnit> findByDepartment(String department);
    List<CourseUnit> findByIsCore(Boolean isCore);
    boolean existsByCode(String code);
}