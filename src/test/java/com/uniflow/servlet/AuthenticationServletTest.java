package com.uniflow.servlet;

import com.uniflow.model.User;
import com.uniflow.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServletTest {

    @Mock
    private AuthService authService;

    private AuthenticationServlet authenticationServlet;

    @BeforeEach
    void setUp() {
        authenticationServlet = new AuthenticationServlet();
        ReflectionTestUtils.setField(authenticationServlet, "authService", authService);
    }

    @Test
    void loginShouldCreateSessionAndForwardToIndexJsp() throws Exception {
        User user = new User();
        user.setId(5L);
        user.setName("Dr. Jane Doe");
        user.setEmail("jane@uniflow.edu");
        user.setRole("COD");
        user.setDepartment("Computer Science");

        when(authService.authenticateUser(anyString(), anyString())).thenReturn(user);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("POST");
        request.setRequestURI("/api/auth/login");
        request.setServletPath("/api/auth");
        request.setPathInfo("/login");
        request.setContentType("application/json");
        request.setContent("{\"email\":\"jane@uniflow.edu\",\"password\":\"secret\"}".getBytes());

        MockHttpServletResponse response = new MockHttpServletResponse();

        authenticationServlet.doPost(request, response);

        HttpSession session = request.getSession(false);
        assertNotNull(session);
        assertEquals(user, session.getAttribute("user"));
        assertEquals("/WEB-INF/jsp/index.jsp", response.getForwardedUrl());
    }

    @Test
    void logoutShouldInvalidateSessionAndForwardToIndexJsp() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("GET");
        request.setRequestURI("/api/auth/logout");
        request.setServletPath("/api/auth");
        request.setPathInfo("/logout");
        request.setSession(new MockHttpSessionWithRole());

        MockHttpServletResponse response = new MockHttpServletResponse();

        authenticationServlet.doGet(request, response);

        assertEquals("/WEB-INF/jsp/index.jsp", response.getForwardedUrl());
    }

    private static class MockHttpSessionWithRole extends org.springframework.mock.web.MockHttpSession {
        MockHttpSessionWithRole() {
            setAttribute("userRole", "COD");
            setAttribute("user", new HashMap<>());
        }
    }
}
