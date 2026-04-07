package com.uniflow.repository;

import com.uniflow.model.ClassRepFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ClassRepFeedbackRepository extends JpaRepository<ClassRepFeedback, Long> {
    List<ClassRepFeedback> findAllByOrderByReportedDateDesc();
    List<ClassRepFeedback> findByStatus(String status);
    List<ClassRepFeedback> findByReportedBy(String reportedBy);
}