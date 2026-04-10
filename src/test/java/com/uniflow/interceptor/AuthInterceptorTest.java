package com.uniflow.interceptor;

import com.uniflow.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuthInterceptorTest {

    private AuthInterceptor authInterceptor;

    @BeforeEach
    void setUp() {
        authInterceptor = new AuthInterceptor();
    }

    @Test
    void studentCanReadOnlyRequestedResources() throws Exception {
        assertTrue(isAllowed("GET", "/api/timetable"));
        assertTrue(isAllowed("GET", "/api/venues"));
        assertTrue(isAllowed("GET", "/api/venues/live-map"));
        assertTrue(isAllowed("GET", "/api/trips"));
        assertFalse(isAllowed("GET", "/api/venues/available"));
    }

    @Test
    void studentCannotMutateAnyResource() throws Exception {
        assertFalse(isAllowed("POST", "/api/timetable"));
        assertFalse(isAllowed("PUT", "/api/venues/5"));
        assertFalse(isAllowed("PATCH", "/api/trips/1/status"));
        assertFalse(isAllowed("DELETE", "/api/bookings/10"));
    }

    private boolean isAllowed(String method, String path) throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod(method);
        request.setRequestURI(path);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", new User());
        session.setAttribute("userRole", User.STUDENT_ROLE);
        session.setAttribute("lastActivity", System.currentTimeMillis());
        request.setSession(session);

        MockHttpServletResponse response = new MockHttpServletResponse();
        return authInterceptor.preHandle(request, response, new Object());
    }
}
