package com.uniflow.service;

import com.uniflow.model.CourseUnitRequest;
import com.uniflow.model.CourseUnit;
import com.uniflow.model.User;
import com.uniflow.repository.RequestRepository;
import com.uniflow.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestServiceTest {

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private RequestService requestService;

    private CourseUnitRequest request;

    @BeforeEach
    void setUp() {
        CourseUnit courseUnit = new CourseUnit();
        courseUnit.setName("Data Structures and Algorithms");

        request = new CourseUnitRequest();
        request.setCourseUnit(courseUnit);
        request.setRequestingDepartment("Computer Science");
        request.setProvidingDepartment("Mathematics");
        request.setExpectedStudents(120);
    }

    @Test
    void createRequestShouldBlockSubmissionWhenPendingIncomingRequestsExist() {
        when(requestRepository.findPendingRequestsForDepartment("Computer Science"))
            .thenReturn(List.of(new CourseUnitRequest()));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> requestService.createRequest(request));

        assertEquals("Cannot submit to DET: You have unacknowledged Service Requests.", exception.getMessage());
        verify(requestRepository, never()).save(any());
    }

    @Test
    void createRequestShouldSaveWhenNoPendingIncomingRequestsExist() {
        when(requestRepository.findPendingRequestsForDepartment("Computer Science"))
            .thenReturn(List.of());
        when(requestRepository.save(any(CourseUnitRequest.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        CourseUnitRequest saved = requestService.createRequest(request);

        assertEquals("PENDING", saved.getStatus());
        assertEquals(request.getRequestingDepartment(), saved.getRequestingDepartment());
        assertEquals(request.getProvidingDepartment(), saved.getProvidingDepartment());
        verify(requestRepository).save(any(CourseUnitRequest.class));
    }

    @Test
    void rejectRequestShouldSetReasonAndCreateNotificationForRequestingCod() {
        CourseUnitRequest existing = new CourseUnitRequest();
        existing.setId(1L);
        existing.setCourseUnit(request.getCourseUnit());
        existing.setRequestingDepartment("Computer Science");
        when(requestRepository.findById(1L)).thenReturn(java.util.Optional.of(existing));
        when(requestRepository.save(any(CourseUnitRequest.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User cod = new User();
        cod.setId(9L);
        cod.setDepartment("Computer Science");
        cod.setRole("COD");
        cod.setIsActive(true);
        when(userRepository.findByDepartmentAndRoleAndIsActiveTrue("Computer Science", "COD")).thenReturn(List.of(cod));

        CourseUnitRequest rejected = requestService.rejectRequest(1L, "Venue clash");

        assertEquals("REJECTED", rejected.getStatus());
        assertEquals("Venue clash", rejected.getRejectionReason());
        verify(notificationService).createNotification(9L, "Request Rejected", "Your course request for Data Structures and Algorithms was rejected: Venue clash", "ALERT");
    }

    @Test
    void resubmitRequestShouldReturnRejectedRequestToPendingWithUpdatedDetails() {
        CourseUnitRequest existing = new CourseUnitRequest();
        existing.setId(2L);
        existing.setStatus("REJECTED");
        existing.setRequestingDepartment("Computer Science");
        existing.setExpectedStudents(100);
        existing.setComments("Initial request");
        when(requestRepository.findById(2L)).thenReturn(java.util.Optional.of(existing));
        when(requestRepository.save(any(CourseUnitRequest.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CourseUnitRequest resubmitted = requestService.resubmitRequest(2L, 130, "Updated cohort size and requirements");

        assertEquals("PENDING", resubmitted.getStatus());
        assertEquals(130, resubmitted.getExpectedStudents());
        assertEquals("Updated cohort size and requirements", resubmitted.getComments());
        assertEquals(null, resubmitted.getRejectionReason());
        verify(requestRepository).save(any(CourseUnitRequest.class));
    }
}
