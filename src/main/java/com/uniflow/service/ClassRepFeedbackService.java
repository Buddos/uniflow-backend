package com.uniflow.service;

import com.uniflow.model.ClassRepFeedback;
import com.uniflow.repository.ClassRepFeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClassRepFeedbackService {

    @Autowired
    private ClassRepFeedbackRepository feedbackRepository;

    public List<ClassRepFeedback> getAllFeedback() {
        return feedbackRepository.findAllByOrderByReportedDateDesc();
    }

    public ClassRepFeedback submitFeedback(ClassRepFeedback feedback) {
        return feedbackRepository.save(feedback);
    }

    public ClassRepFeedback getFeedbackById(Long id) {
        return feedbackRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Feedback not found"));
    }

    public ClassRepFeedback updateFeedbackStatus(Long id, String status) {
        ClassRepFeedback feedback = getFeedbackById(id);
        feedback.setStatus(status);
        return feedbackRepository.save(feedback);
    }

    public List<ClassRepFeedback> getFeedbackByStatus(String status) {
        return feedbackRepository.findByStatus(status);
    }

    public List<ClassRepFeedback> getFeedbackByReporter(String reportedBy) {
        return feedbackRepository.findByReportedBy(reportedBy);
    }
}