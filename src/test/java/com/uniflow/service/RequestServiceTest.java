package com.uniflow.service;

import com.uniflow.model.CourseUnitRequest;
import com.uniflow.repository.RequestRepository;
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

    @InjectMocks
    private RequestService requestService;

    private CourseUnitRequest request;

    @BeforeEach
    void setUp() {
        request = new CourseUnitRequest();
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
}
