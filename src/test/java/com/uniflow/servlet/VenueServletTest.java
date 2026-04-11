package com.uniflow.servlet;

import com.uniflow.model.AcademicTrip;
import com.uniflow.model.User;
import com.uniflow.model.Venue;
import com.uniflow.service.TripService;
import com.uniflow.service.VenueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VenueServletTest {

    @Mock
    private VenueService venueService;

    @Mock
    private TripService tripService;

    private VenueServlet venueServlet;

    @BeforeEach
    void setUp() {
        venueServlet = new VenueServlet();
        ReflectionTestUtils.setField(venueServlet, "venueService", venueService);
        ReflectionTestUtils.setField(venueServlet, "tripService", tripService);
    }

    @Test
    void doGetShouldForwardLiveMapWithVenuesAndTrips() throws Exception {
        Venue venue = new Venue();
        venue.setName("PST1");
        venue.setStatus("AVAILABLE");
        venue.setCapacity(200);

        AcademicTrip trip = new AcademicTrip();
        trip.setTitle("Faculty Field Work");
        trip.setDepartment("Computer Science");
        trip.setCohort("CS2026");
        trip.setStatus("ACTIVE");

        when(venueService.getAllVenues()).thenReturn(List.of(venue));
        when(tripService.getActiveTrips()).thenReturn(List.of(trip));

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("GET");
        request.setRequestURI("/api/venues/live-map");
        request.setServletPath("/api/venues");
        request.setPathInfo("/live-map");
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", new User());
        session.setAttribute("userRole", "COD");
        request.setSession(session);

        MockHttpServletResponse response = new MockHttpServletResponse();

        venueServlet.doGet(request, response);

        assertEquals("/WEB-INF/jsp/liveMap.jsp", response.getForwardedUrl());
        assertEquals(1, ((List<?>) request.getAttribute("venues")).size());
        assertEquals(1, ((List<?>) request.getAttribute("activeTrips")).size());
    }
}
