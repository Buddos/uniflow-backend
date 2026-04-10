package com.uniflow.service;

import com.uniflow.model.CourseUnitRequest;
import com.uniflow.model.User;
import com.uniflow.repository.RequestRepository;
import com.uniflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
@Service
public class RequestService {
    
    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;
    
    @Transactional
    public CourseUnitRequest createRequest(CourseUnitRequest request) {
        assertNoPendingIncomingRequests(request);

        request.setStatus("PENDING");
        request.setRequestedAt(LocalDateTime.now());
        return requestRepository.save(request);
    }

    private void assertNoPendingIncomingRequests(CourseUnitRequest request) {
        if (request == null || request.getRequestingDepartment() == null || request.getRequestingDepartment().isBlank()) {
            throw new IllegalStateException("Submitting department is required");
        }

        if (!requestRepository.findPendingRequestsForDepartment(request.getRequestingDepartment()).isEmpty()) {
            throw new IllegalStateException("Cannot submit to DET: You have unacknowledged Service Requests.");
        }
    }
    
    @Transactional
    public CourseUnitRequest acceptRequest(Long requestId) {
        CourseUnitRequest request = requestRepository.findById(requestId)
            .orElseThrow(() -> new RuntimeException("Request not found"));
        
        request.setStatus("ACCEPTED");
        request.setRespondedAt(LocalDateTime.now());
        return requestRepository.save(request);
    }
    
    @Transactional
    public CourseUnitRequest rejectRequest(Long requestId, String reason) {
        CourseUnitRequest request = requestRepository.findById(requestId)
            .orElseThrow(() -> new RuntimeException("Request not found"));

        if (reason == null || reason.isBlank()) {
            throw new IllegalStateException("Rejection reason is required");
        }
        
        request.setStatus("REJECTED");
        request.setRejectionReason(reason);
        request.setRespondedAt(LocalDateTime.now());

        CourseUnitRequest savedRequest = requestRepository.save(request);
        notifyRequestingCodOfRejection(savedRequest, reason);
        return savedRequest;
    }

    @Transactional
    public CourseUnitRequest resubmitRequest(Long requestId, Integer expectedStudents, String comments) {
        CourseUnitRequest request = requestRepository.findById(requestId)
            .orElseThrow(() -> new RuntimeException("Request not found"));

        if (!"REJECTED".equals(request.getStatus())) {
            throw new IllegalStateException("Only rejected requests can be resubmitted");
        }

        request.setExpectedStudents(expectedStudents);
        request.setComments(comments);
        request.setStatus("PENDING");
        request.setRejectionReason(null);
        request.setRespondedAt(null);
        request.setRequestedAt(LocalDateTime.now());
        return requestRepository.save(request);
    }
    
    public List<CourseUnitRequest> getPendingRequestsForDepartment(String department) {
        return requestRepository.findPendingRequestsForDepartment(department);
    }
    
    public List<CourseUnitRequest> getAllRequests() {
        return requestRepository.findAll();
    }
    
    public List<CourseUnitRequest> getRequestsByRequestingDepartment(String department) {
        return requestRepository.findByRequestingDepartment(department);
    }
    
    public Map<String, Object> getRequestStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("pending", requestRepository.countPendingRequests());
        stats.put("total", requestRepository.count());
        stats.put("accepted", requestRepository.findByStatus("ACCEPTED").size());
        stats.put("rejected", requestRepository.findByStatus("REJECTED").size());
        return stats;
    }
    
    @Transactional
    public void enforceDeadline(String currentSemester, String previousSemester) {
        List<CourseUnitRequest> prevRequests = requestRepository.findBySemester(previousSemester);
        Set<String> prevDepts = prevRequests.stream()
                .map(CourseUnitRequest::getRequestingDepartment)
                .collect(Collectors.toSet());
                
        List<CourseUnitRequest> currRequests = requestRepository.findBySemester(currentSemester);
        Set<String> currDepts = currRequests.stream()
                .map(CourseUnitRequest::getRequestingDepartment)
                .collect(Collectors.toSet());
                
        for (String dept : prevDepts) {
            if (!currDepts.contains(dept)) {
                List<CourseUnitRequest> deptPrevRequests = prevRequests.stream()
                    .filter(r -> r.getRequestingDepartment().equals(dept) && "ACCEPTED".equals(r.getStatus()))
                    .collect(Collectors.toList());
                
                for (CourseUnitRequest prevReq : deptPrevRequests) {
                    CourseUnitRequest newReq = new CourseUnitRequest();
                    newReq.setCourseUnit(prevReq.getCourseUnit());
                    newReq.setRequestingDepartment(dept);
                    newReq.setProvidingDepartment(prevReq.getProvidingDepartment());
                    newReq.setExpectedStudents(prevReq.getExpectedStudents());
                    newReq.setStatus("ACCEPTED");
                    newReq.setSemester(currentSemester);
                    newReq.setIsAutoFallback(true);
                    newReq.setRequestedAt(LocalDateTime.now());
                    newReq.setRespondedAt(LocalDateTime.now());
                    requestRepository.save(newReq);
                }
            }
        }
    }

    private void notifyRequestingCodOfRejection(CourseUnitRequest request, String reason) {
        if (request.getRequestingDepartment() == null || request.getRequestingDepartment().isBlank()) {
            return;
        }

        List<User> requesters = userRepository.findByDepartmentAndRoleAndIsActiveTrue(request.getRequestingDepartment(), "COD");
        if (requesters.isEmpty()) {
            return;
        }

        User requestingCod = requesters.get(0);
        String courseName = request.getCourseUnit() != null ? request.getCourseUnit().getName() : "the request";
        notificationService.createNotification(
            requestingCod.getId(),
            "Request Rejected",
            "Your course request for " + courseName + " was rejected: " + reason,
            "ALERT"
        );
    }
}