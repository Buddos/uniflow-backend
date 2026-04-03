package com.uniflow.dto;

import javax.validation.constraints.NotNull;

public class CourseRequestDTO {
    @NotNull(message = "Course unit ID is required")
    private Long courseUnitId;
    
    @NotNull(message = "Expected students count is required")
    private Integer expectedStudents;
    
    private String requestingDepartmentCode;
    
    private String comments;
    
    // Getters and Setters
    public Long getCourseUnitId() { return courseUnitId; }
    public void setCourseUnitId(Long courseUnitId) { this.courseUnitId = courseUnitId; }
    
    public Integer getExpectedStudents() { return expectedStudents; }
    public void setExpectedStudents(Integer expectedStudents) { this.expectedStudents = expectedStudents; }
    
    public String getRequestingDepartmentCode() { return requestingDepartmentCode; }
    public void setRequestingDepartmentCode(String requestingDepartmentCode) { this.requestingDepartmentCode = requestingDepartmentCode; }
    
    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
}