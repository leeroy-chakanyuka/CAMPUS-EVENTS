package za.ac.cput.campus_events.repository;
/*
Mologadi Dikgale
Student Number: 231016263
 */

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.ac.cput.campus_events.domain.Admin;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminRepositoryTest {

    @Mock
    private AdminRepository adminRepository;
    private Admin admin;

    @BeforeEach
    void setUp() {
        admin = new Admin.Builder()
                .setFirstName("Peter")
                .setLastName("Jones")
                .setEmail("peter@cput.ac.za")
                .setPassword("mycputpeter")
                .build();
    }

    @Test
    void testSave_ShouldReturnSavedAdmin() {
        when(adminRepository.save(admin)).thenReturn(admin);
        Admin saved = adminRepository.save(admin);
        assertNotNull(saved);
        assertEquals("Peter",           saved.getFirstName());
        assertEquals("Jones",           saved.getLastName());
        assertEquals("peter@cput.ac.za",saved.getEmail());
        verify(adminRepository, times(1)).save(admin);
    }

    @Test
    void testFindById_ExistingId_ShouldReturnAdmin() {
        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));
        Optional<Admin> result = adminRepository.findById(1L);
        assertTrue(result.isPresent());
        assertEquals("Peter", result.get().getFirstName());
    }

    @Test
    void testFindById_NonExistingId_ShouldReturnEmpty() {
        when(adminRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<Admin> result = adminRepository.findById(99L);
        assertFalse(result.isPresent());
    }

    @Test
    void testFindAll_ShouldReturnList() {
        when(adminRepository.findAll()).thenReturn(List.of(admin));
        List<Admin> result = adminRepository.findAll();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testDeleteById_ShouldCallRepository() {
        adminRepository.deleteById(1L);
        verify(adminRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindByEmail_ExistingEmail_ShouldReturnAdmin() {
        when(adminRepository.findByEmail("peter@cput.ac.za"))
                .thenReturn(Optional.of(admin));
        Optional<Admin> result = adminRepository.findByEmail("peter@cput.ac.za");
        assertTrue(result.isPresent());
        assertEquals("peter@cput.ac.za", result.get().getEmail());
    }

    @Test
    void testFindByEmail_NonExistingEmail_ShouldReturnEmpty() {
        when(adminRepository.findByEmail("unknown@cput.ac.za"))
                .thenReturn(Optional.empty());
        Optional<Admin> result = adminRepository.findByEmail("unknown@cput.ac.za");
        assertFalse(result.isPresent());
    }
}
