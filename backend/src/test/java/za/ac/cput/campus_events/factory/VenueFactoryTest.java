package za.ac.cput.campus_events.factory;

import org.junit.jupiter.api.Test;
import za.ac.cput.campus_events.domain.Venue;

import static org.junit.jupiter.api.Assertions.*;

class VenueFactoryTest {

    @Test
    void testCreateVenue() {
        Venue venue = VenueFactory.createVenue("Hall A", 200, 1L, "Main Building");
        assertNotNull(venue, "VenueFactory returned null");
        assertEquals("Hall A", venue.getName(), "Venue name mismatch");
        assertEquals(200, venue.getCapacity(), "Venue capacity mismatch");
        assertEquals(1L, venue.getFacultyId(), "Venue facultyId mismatch");
        assertEquals("Main Building", venue.getLocation(), "Venue location mismatch");
    }
}
