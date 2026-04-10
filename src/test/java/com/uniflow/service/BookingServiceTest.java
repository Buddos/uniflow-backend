package com.uniflow.service;

import com.uniflow.exception.ProximityViolationException;
import com.uniflow.model.Booking;
import com.uniflow.model.Venue;
import com.uniflow.repository.BookingRepository;
import com.uniflow.repository.UserRepository;
import com.uniflow.repository.VenueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private VenueRepository venueRepository;

    @InjectMocks
    private BookingService bookingService;

    private Booking booking;

    @BeforeEach
    void setUp() {
        Venue venue = new Venue();
        venue.setId(1L);
        venue.setName("PST1");
        venue.setStatus("AVAILABLE");

        booking = new Booking();
        booking.setVenue(venue);
        booking.setStartTime(LocalDateTime.now().plusHours(2));
        booking.setEndTime(LocalDateTime.now().plusHours(4));
        booking.setPurpose("Module IV practical");
    }

    @Test
    void createBookingShouldThrowWhenVenueDistanceExceeds300Meters() {
        booking.getVenue().setDistanceFromOfficeMeters(301);

        ProximityViolationException exception = assertThrows(ProximityViolationException.class, () -> bookingService.createBooking(booking));

        assertEquals("Cannot assign venue: Location exceeds the 300m equipment proximity limit.", exception.getMessage());
        verify(bookingRepository, never()).save(any(Booking.class));
        verify(venueRepository, never()).save(any(Venue.class));
    }

    @Test
    void createBookingShouldSucceedWhenVenueDistanceIsWithinLimit() {
        booking.getVenue().setDistanceFromOfficeMeters(220);
        when(bookingRepository.findConflictingBookings(any(Venue.class), any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(List.of());
        when(venueRepository.save(any(Venue.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Booking saved = bookingService.createBooking(booking);

        assertEquals("CONFIRMED", saved.getStatus());
        assertNotNull(saved.getCreatedAt());
        verify(venueRepository).save(any(Venue.class));
        verify(bookingRepository).save(any(Booking.class));
    }
}
