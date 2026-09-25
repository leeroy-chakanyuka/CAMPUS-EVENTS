package za.ac.cput.campus_events.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.ac.cput.campus_events.domain.Address;
import za.ac.cput.campus_events.domain.Venue;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VenueRepositoryTest {

    @Mock
    private VenueRepository venueRepository;

    private Venue venue;

    @BeforeEach
    void setUp() {
        Address address = new Address.Builder()
                .setStreet("123 Adderley St")
                .setCity("Cape Town")
                .setProvince("Western Cape")
                .setPostalCode("8000")
                .build();

        venue = new Venue.Builder()
                .setName("Hall B")
                .setCapacity(150)
                .setAddress(address)
                .build();
    }

    @Test
    void testSave_ShouldReturnSavedVenue() {
        when(venueRepository.save(venue)).thenReturn(venue);

        Venue saved = venueRepository.save(venue);

        assertNotNull(saved);
        assertEquals("Hall B", saved.getName());
        verify(venueRepository, times(1)).save(venue);
    }

    @Test
    void testFindById_ExistingId_ShouldReturnVenue() {
        when(venueRepository.findById(1L)).thenReturn(Optional.of(venue));

        Optional<Venue> result = venueRepository.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Hall B", result.get().getName());
    }

    @Test
    void testFindById_NonExistingId_ShouldReturnEmpty() {
        when(venueRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Venue> result = venueRepository.findById(99L);

        assertFalse(result.isPresent());
    }

    @Test
    void testFindAll_ShouldReturnList() {
        when(venueRepository.findAll()).thenReturn(List.of(venue));

        List<Venue> result = venueRepository.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testDeleteById_ShouldCallRepository() {
        venueRepository.deleteById(1L);

        verify(venueRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindByName_ShouldReturnList() {
        when(venueRepository.findByName("Hall B")).thenReturn(List.of(venue));

        List<Venue> result = venueRepository.findByName("Hall B");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Hall B", result.get(0).getName());
    }

    @Test
    void testFindByCapacityGreaterThan_ShouldReturnList() {
        when(venueRepository.findByCapacityGreaterThan(100)).thenReturn(List.of(venue));

        List<Venue> result = venueRepository.findByCapacityGreaterThan(100);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getCapacity() > 100);
    }

    @Test
    void testFindByCity_ShouldReturnList() {
        when(venueRepository.findByAddress_City("Cape Town")).thenReturn(List.of(venue));

        List<Venue> result = venueRepository.findByAddress_City("Cape Town");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Cape Town", result.get(0).getAddress().getCity());
    }
}
