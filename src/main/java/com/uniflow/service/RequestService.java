package com.uniflow.service;

import com.uniflow.model.CourseUnitRequest;
import com.uniflow.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RequestService {
    
    @Autowired
    private RequestRepository requestRepository;
    
    public CourseUnitRequest createRequest(CourseUnitRequest request) {
        request.setStatus("PENDING");
        request.setRequestedAt(LocalDateTime.now());
        return requestRepository.save(request);
    }
    
    public CourseUnitRequest acceptRequest(Long requestId) {
        CourseUnitRequest request = requestRepository.findById(requestId)
            .orElseThrow(() -> new RuntimeException("Request not found"));
        
        request.setStatus("ACCEPTED");
        request.setRespondedAt(LocalDateTime.now());
        return requestRepository.save(request);
    }
    
    public CourseUnitRequest rejectRequest(Long requestId, String reason) {
        CourseUnitRequest request = requestRepository.findById(requestId)
            .orElseThrow(() -> new RuntimeException("Request not found"));
        
        request.setStatus("REJECTED");
        request.setRejectionReason(reason);
        request.setRespondedAt(LocalDateTime.now());
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
}