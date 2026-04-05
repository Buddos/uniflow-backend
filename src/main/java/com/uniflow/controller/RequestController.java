package com.uniflow.controller;

import com.uniflow.dto.CourseRequestDTO;
import com.uniflow.model.CourseUnit;
import com.uniflow.model.CourseUnitRequest;
import com.uniflow.repository.CourseUnitRepository;
import com.uniflow.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/requests")
@CrossOrigin(origins = "*")
public class RequestController {
    
    @Autowired
    private RequestService requestService;
    
    @Autowired
    private CourseUnitRepository courseUnitRepository;
    
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllRequests() {
        List<CourseUnitRequest> requests = requestService.getAllRequests();
        List<Map<String, Object>> courseRequests = requests.stream().map(request -> {
            Map<String, Object> cr = new HashMap<>();
            cr.put("id", request.getId().toString());
            cr.put("courseUnit", Map.of(
                "id", request.getCourseUnit().getId().toString(),
                "code", request.getCourseUnit().getCode(),
                "name", request.getCourseUnit().getName(),
                "department", request.getCourseUnit().getDepartment(),
                "creditHours", request.getCourseUnit().getCreditHours()
            ));
            cr.put("requestingDepartment", request.getRequestingDepartment());
            cr.put("providingDepartment", request.getProvidingDepartment());
            cr.put("cohortSize", request.getExpectedStudents());
            cr.put("status", request.getStatus() != null ? request.getStatus().toLowerCase() : "pending");
            cr.put("createdAt", request.getRequestedAt() != null ? request.getRequestedAt().toString() : "2026-03-20");
            return cr;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(courseRequests);
    }
    
    @GetMapping("/pending/{department}")
    public ResponseEntity<List<CourseUnitRequest>> getPendingRequests(@PathVariable String department) {
        return ResponseEntity.ok(requestService.getPendingRequestsForDepartment(department));
    }
    
    @PostMapping
    public ResponseEntity<CourseUnitRequest> createRequest(@RequestBody CourseRequestDTO requestDTO) {
        CourseUnitRequest request = new CourseUnitRequest();
        
        CourseUnit courseUnit = courseUnitRepository.findById(requestDTO.getCourseUnitId())
            .orElseThrow(() -> new RuntimeException("Course unit not found"));
        
        request.setCourseUnit(courseUnit);
        request.setRequestingDepartment(requestDTO.getRequestingDepartmentCode());
        request.setExpectedStudents(requestDTO.getExpectedStudents());
        request.setProvidingDepartment(courseUnit.getDepartment());
        
        return ResponseEntity.ok(requestService.createRequest(request));
    }
    
    @PostMapping("/{requestId}/accept")
    public ResponseEntity<CourseUnitRequest> acceptRequest(@PathVariable Long requestId) {
        return ResponseEntity.ok(requestService.acceptRequest(requestId));
    }
    
    @PostMapping("/{requestId}/reject")
    public ResponseEntity<CourseUnitRequest> rejectRequest(
            @PathVariable Long requestId,
            @RequestParam String reason) {
        return ResponseEntity.ok(requestService.rejectRequest(requestId, reason));
    }
    
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        return ResponseEntity.ok(requestService.getRequestStatistics());
    }
    
    @PostMapping("/admin/enforce-deadline")
    public ResponseEntity<Map<String, String>> enforceDeadline(
            @RequestParam String currentSemester,
            @RequestParam String previousSemester) {
        requestService.enforceDeadline(currentSemester, previousSemester);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Deadline enforced and fallback data applied successfully");
        return ResponseEntity.ok(response);
    }
}