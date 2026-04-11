package com.uniflow.servlet;

import com.uniflow.model.CourseUnit;
import com.uniflow.model.CourseUnitRequest;
import com.uniflow.repository.CourseUnitRepository;
import com.uniflow.service.RealtimeService;
import com.uniflow.service.RequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestServletTest {

    @Mock
    private RequestService requestService;

    @Mock
    private CourseUnitRepository courseUnitRepository;

    @Mock
    private RealtimeService realtimeService;

    private RequestServlet requestServlet;

    @BeforeEach
    void setUp() {
        requestServlet = new RequestServlet();
        ReflectionTestUtils.setField(requestServlet, "requestService", requestService);
        ReflectionTestUtils.setField(requestServlet, "courseUnitRepository", courseUnitRepository);
        ReflectionTestUtils.setField(requestServlet, "realtimeService", realtimeService);
    }

    @Test
    void doGetShouldForwardHandshakeViewWithIncomingOutgoingAndSettledRequests() throws Exception {
        CourseUnit incomingCourse = new CourseUnit();
        incomingCourse.setId(1L);
        incomingCourse.setCode("CSC101");
        incomingCourse.setName("Foundations of CS");
        incomingCourse.setDepartment("Computer Science");

        CourseUnit outgoingCourse = new CourseUnit();
        outgoingCourse.setId(2L);
        outgoingCourse.setCode("MTH201");
        outgoingCourse.setName("Linear Algebra");
        outgoingCourse.setDepartment("Mathematics");

        CourseUnitRequest incoming = new CourseUnitRequest();
        incoming.setId(10L);
        incoming.setCourseUnit(incomingCourse);
        incoming.setRequestingDepartment("Mathematics");
        incoming.setProvidingDepartment("Computer Science");
        incoming.setStatus("PENDING");

        CourseUnitRequest outgoing = new CourseUnitRequest();
        outgoing.setId(11L);
        outgoing.setCourseUnit(outgoingCourse);
        outgoing.setRequestingDepartment("Computer Science");
        outgoing.setProvidingDepartment("Mathematics");
        outgoing.setStatus("PENDING");

        CourseUnitRequest settled = new CourseUnitRequest();
        settled.setId(12L);
        settled.setCourseUnit(outgoingCourse);
        settled.setRequestingDepartment("Computer Science");
        settled.setProvidingDepartment("Mathematics");
        settled.setStatus("REJECTED");

        when(requestService.getAllRequests()).thenReturn(List.of(incoming, outgoing, settled));

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("GET");
        request.setRequestURI("/api/requests");
        request.setServletPath("/api/requests");
        request.addHeader("Accept", "text/html");
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("department", "Computer Science");
        request.setSession(session);

        MockHttpServletResponse response = new MockHttpServletResponse();

        requestServlet.doGet(request, response);

        assertEquals("/WEB-INF/jsp/courserequest.jsp", response.getForwardedUrl());
        assertEquals(3, ((List<?>) request.getAttribute("requests")).size());
        assertEquals(1, ((List<?>) request.getAttribute("pendingIncoming")).size());
        assertEquals(2, ((List<?>) request.getAttribute("pendingOutgoing")).size());
        assertEquals(1, ((List<?>) request.getAttribute("settledRequests")).size());
    }

    @Test
    void doPostShouldCreateRequestFromFormAndRedirect() throws Exception {
        CourseUnit courseUnit = new CourseUnit();
        courseUnit.setId(1L);
        courseUnit.setDepartment("Mathematics");

        CourseUnitRequest savedRequest = new CourseUnitRequest();
        savedRequest.setId(20L);
        savedRequest.setStatus("PENDING");

        when(requestService.getPendingRequestsForDepartment("Computer Science")).thenReturn(List.of());
        when(courseUnitRepository.findById(1L)).thenReturn(Optional.of(courseUnit));
        when(requestService.createRequest(any(CourseUnitRequest.class))).thenReturn(savedRequest);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("POST");
        request.setRequestURI("/api/requests");
        request.setServletPath("/api/requests");
        request.addParameter("courseUnitId", "1");
        request.addParameter("cohortSize", "120");
        request.addParameter("requestingDept", "Computer Science");
        request.addParameter("comments", "Need venue support");

        MockHttpServletResponse response = new MockHttpServletResponse();

        requestServlet.doPost(request, response);

        assertEquals(302, response.getStatus());
        assertTrue(response.getRedirectedUrl().endsWith("/view/courserequest?success=1"));

        ArgumentCaptor<CourseUnitRequest> requestCaptor = ArgumentCaptor.forClass(CourseUnitRequest.class);
        verify(requestService).createRequest(requestCaptor.capture());
        assertEquals("Computer Science", requestCaptor.getValue().getRequestingDepartment());
        assertEquals(Integer.valueOf(120), requestCaptor.getValue().getExpectedStudents());
        assertEquals("Mathematics", requestCaptor.getValue().getProvidingDepartment());
    }

    @Test
    void doPostShouldBlockSubmissionWhenLockIsActive() throws Exception {
        CourseUnit courseUnit = new CourseUnit();
        courseUnit.setId(1L);
        courseUnit.setDepartment("Mathematics");

        when(requestService.getPendingRequestsForDepartment("Computer Science")).thenReturn(List.of(new CourseUnitRequest()));

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("POST");
        request.setRequestURI("/api/requests");
        request.setServletPath("/api/requests");
        request.addParameter("courseUnitId", "1");
        request.addParameter("cohortSize", "120");
        request.addParameter("requestingDept", "Computer Science");

        MockHttpServletResponse response = new MockHttpServletResponse();

        requestServlet.doPost(request, response);

        assertEquals(409, response.getStatus());
        assertTrue(response.getContentAsString().contains("unacknowledged Service Requests"));
        verify(requestService, never()).createRequest(any(CourseUnitRequest.class));
    }

    @Test
    void doPostShouldProcessRequestViaLegacyRejectRoute() throws Exception {
        CourseUnitRequest rejectedRequest = new CourseUnitRequest();
        rejectedRequest.setId(42L);
        rejectedRequest.setStatus("REJECTED");

        when(requestService.rejectRequest(42L, "Insufficient capacity")).thenReturn(rejectedRequest);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("POST");
        request.setRequestURI("/api/requests/42/reject");
        request.setServletPath("/api/requests");
        request.setPathInfo("/42/reject");
        request.setContentType("application/json");
        request.addParameter("reason", "Insufficient capacity");

        MockHttpServletResponse response = new MockHttpServletResponse();

        requestServlet.doPost(request, response);

        assertEquals(200, response.getStatus());
        assertTrue(response.getContentAsString().contains("REJECTED"));
        verify(realtimeService).broadcastRequestChange(any(), any());
    }
}