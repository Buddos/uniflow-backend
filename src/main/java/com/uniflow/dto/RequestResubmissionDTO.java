package com.uniflow.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class RequestResubmissionDTO {

    @NotNull(message = "Expected students count is required")
    @Min(value = 1, message = "Expected students must be at least 1")
    private Integer expectedStudents;

    private String comments;

    public Integer getExpectedStudents() { return expectedStudents; }
    public void setExpectedStudents(Integer expectedStudents) { this.expectedStudents = expectedStudents; }

    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
}