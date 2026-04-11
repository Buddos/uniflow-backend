package com.uniflow.servlet;

import com.uniflow.dto.TripRequestDTO;
import com.uniflow.model.AcademicTrip;
import com.uniflow.model.User;
import com.uniflow.model.Venue;
import com.uniflow.service.RealtimeService;
import com.uniflow.service.TripService;
import com.uniflow.service.VenueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TripServletTest {

    @Mock
    private TripService tripService;

    @Mock
    private VenueService venueService;

    @Mock
    private RealtimeService realtimeService;

    private TripServlet tripServlet;

    @BeforeEach
    void setUp() {
        tripServlet = new TripServlet();
        ReflectionTestUtils.setField(tripServlet, "tripService", tripService);
        ReflectionTestUtils.setField(tripServlet, "venueService", venueService);
        ReflectionTestUtils.setField(tripServlet, "realtimeService", realtimeService);
    }

    @Test
    void doPostShouldLogTripAndForwardToLiveMap() throws Exception {
        Venue venue = new Venue();
        venue.setName("PST1");
        venue.setStatus("AVAILABLE");
        venue.setCapacity(200);

        AcademicTrip trip = new AcademicTrip();
        trip.setTitle("Faculty Retreat");
        trip.setDepartment("Computer Science");
        trip.setCohort("CS2026");
        trip.setStartDate(LocalDate.parse("2026-04-11"));
        trip.setEndDate(LocalDate.parse("2026-04-13"));
        trip.setStatus("ACTIVE");

        when(tripService.createTrip(any(AcademicTrip.class))).thenReturn(trip);
        when(tripService.getActiveTrips()).thenReturn(List.of(trip));
        when(venueService.getAllVenues()).thenReturn(List.of(venue));
        doNothing().when(realtimeService).broadcastTripChange(any(), any());

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("POST");
        request.setRequestURI("/api/trips");
        request.setServletPath("/api/trips");
        request.setContentType("application/json");
        request.setContent(("{" +
            "\"title\":\"Faculty Retreat\"," +
            "\"destination\":\"Naivasha\"," +
            "\"cohort\":\"CS2026\"," +
            "\"startDate\":\"2026-04-11\"," +
            "\"endDate\":\"2026-04-13\"," +
            "\"description\":\"Field engagement\"," +
            "\"department\":\"Computer Science\"," +
            "\"numberOfStudents\":35}" ).getBytes());
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", new User());
        session.setAttribute("userRole", "LECTURER");
        request.setSession(session);

        MockHttpServletResponse response = new MockHttpServletResponse();

        tripServlet.doPost(request, response);

        verify(tripService).createTrip(any(AcademicTrip.class));
        verify(realtimeService).broadcastTripChange(any(), any());
        assertEquals(200, response.getStatus());
    }
}
