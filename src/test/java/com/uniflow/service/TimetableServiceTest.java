package com.uniflow.service;

import com.uniflow.model.TimetableEntry;
import com.uniflow.model.Venue;
import com.uniflow.repository.TimetableRepository;
import com.uniflow.repository.UserRepository;
import com.uniflow.repository.VenueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TimetableServiceTest {

    @Mock
    private TimetableRepository timetableRepository;

    @Mock
    private VenueRepository venueRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TimetableService timetableService;

    private TimetableEntry entry;

    @BeforeEach
    void setUp() {
        Venue venue = new Venue();
        venue.setId(1L);
        venue.setName("NPL2");

        entry = new TimetableEntry();
        entry.setVenue(venue);
    }

    @Test
    void createTimetableEntryAcceptsVenueWhenCapacityIsAtLeast110PercentOfAdmitted() {
        entry.getVenue().setCapacity(220);
        entry.setTotalAdmittedStudents(200);
        entry.setRegisteredStudents(0);
        when(timetableRepository.save(any(TimetableEntry.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TimetableEntry saved = timetableService.createTimetableEntry(entry);

        assertEquals(200, saved.getTotalAdmittedStudents());
        verify(timetableRepository).save(any(TimetableEntry.class));
    }

    @Test
    void createTimetableEntryRejectsVenueWhenCapacityIsBelow110PercentEvenIfRegisteredIsZero() {
        entry.getVenue().setCapacity(219);
        entry.setTotalAdmittedStudents(200);
        entry.setRegisteredStudents(0);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> timetableService.createTimetableEntry(entry));

        assertEquals("Venue capacity insufficient. Required: 220, Available: 219", exception.getMessage());
    }
}
