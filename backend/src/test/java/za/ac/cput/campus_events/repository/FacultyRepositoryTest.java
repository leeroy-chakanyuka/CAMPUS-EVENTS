package za.ac.cput.campus_events.repository;
/*
Dikgale Mologadi
student no: 231016263
 */
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.ac.cput.campus_events.domain.Faculty;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

 import org.springframework.data.jpa.repository.JpaRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FacultyRepositoryTest {

    @Mock
    private FacultyRepository facultyRepository;

    private Faculty faculty;

    @BeforeEach
    void setUp() {
        faculty = new Faculty.Builder()
                .name("Faculty of Engineering")
                .status("ACTIVE")
                .contactEmail("engineering@cput.ac.za")
                .createdByAdminId(1L)
                .createdAt(LocalDateTime.now())
                .build();

    }

    @Test
    void testSave_ShouldReturnSavedFaculty() {
        when(facultyRepository.save(faculty)).thenReturn(faculty);
        Faculty saved = facultyRepository.save(faculty);
        assertNotNull(saved);
        assertEquals("Faculty of Engineering", saved.getName());
        verify(facultyRepository, times(1)).save(faculty);
    }

    @Test
    void testFindById_ExistingId_ShouldReturnFaculty() {
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));
        Optional<Faculty> result = facultyRepository.findById(1L);
        assertTrue(result.isPresent());
        assertEquals("Faculty of Engineering", result.get().getName());
    }

    @Test
    void testFindById_NonExistingId_ShouldReturnEmpty() {
        when(facultyRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<Faculty> result = facultyRepository.findById(99L);
        assertFalse(result.isPresent());
    }

    @Test
    void testFindAll_ShouldReturnList() {
        when(facultyRepository.findAll()).thenReturn(List.of(faculty));
        List<Faculty> result = facultyRepository.findAll();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testDeleteById_ShouldCallRepository() {
        facultyRepository.deleteById(1L);
        verify(facultyRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindByStatus_ActiveStatus_ShouldReturnList() {
        when(facultyRepository.findByStatus("ACTIVE")).thenReturn(List.of(faculty));
        List<Faculty> result = facultyRepository.findByStatus("ACTIVE");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ACTIVE", result.get(0).getStatus());
    }

    @Test
    void testFindByStatus_InactiveStatus_ShouldReturnEmptyList() {
        when(facultyRepository.findByStatus("INACTIVE")).thenReturn(List.of());
        List<Faculty> result = facultyRepository.findByStatus("INACTIVE");
        assertNotNull(result);
        assertEquals(0, result.size());
    }
}
