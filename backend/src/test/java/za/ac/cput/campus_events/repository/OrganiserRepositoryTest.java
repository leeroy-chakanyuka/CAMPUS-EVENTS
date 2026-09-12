package za.ac.cput.campus_events.repository;
/*
Mologadi Dikgale
Student no: 231016263
 */
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.ac.cput.campus_events.domain.Organiser;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class OrganiserRepositoryTest {
    @Mock
    private OrganiserRepository organiserRepository;

    private Organiser organiser;

    @BeforeEach
    void setUp() {
        organiser = new Organiser.Builder()
                .setFirstName("John")
                .setLastName("Smith")
                .setEmail("john@cput.ac.za")
                .setRole("EVENT_COORDINATOR")
                .build();
    }

    @Test
    void testSave_ShouldReturnSavedOrganiser() {
        when(organiserRepository.save(organiser)).thenReturn(organiser);
        Organiser saved = organiserRepository.save(organiser);
        assertNotNull(saved);
        assertEquals("John", saved.getFirstName());
        verify(organiserRepository, times(1)).save(organiser);
    }

    @Test
    void testFindById_ExistingId_ShouldReturnOrganiser() {
        when(organiserRepository.findById(1L)).thenReturn(Optional.of(organiser));
        Optional<Organiser> result = organiserRepository.findById(1L);
        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
    }

    @Test
    void testFindById_NonExistingId_ShouldReturnEmpty() {
        when(organiserRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<Organiser> result = organiserRepository.findById(99L);
        assertFalse(result.isPresent());
    }

    @Test
    void testFindAll_ShouldReturnList() {
        when(organiserRepository.findAll()).thenReturn(List.of(organiser));
        List<Organiser> result = organiserRepository.findAll();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testDeleteById_ShouldCallRepository() {
        organiserRepository.deleteById(1L);
        verify(organiserRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindByFacultyId_ShouldReturnList() {
        when(organiserRepository.findByFacultyId(1L)).thenReturn(List.of(organiser));
        List<Organiser> result = organiserRepository.findByFacultyId(1L);
        assertNotNull(result);
        assertEquals(1, result.size());
    }
}
