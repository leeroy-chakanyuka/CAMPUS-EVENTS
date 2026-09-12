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
import za.ac.cput.campus_events.domain.Student;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentRepositoryTest {
    @Mock
    private StudentRepository studentRepository;

    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student.Builder()
                .setFirstName("Jane")
                .setLastName("Doe")
                .setEmail("jane@cput.ac.za")
                .setStudentNumber("220123456")
                .build();
    }

    @Test
    void testSave_ShouldReturnSavedStudent() {
        when(studentRepository.save(student)).thenReturn(student);
        Student saved = studentRepository.save(student);
        assertNotNull(saved);
        assertEquals("Jane",        saved.getFirstName());
        assertEquals("Doe",         saved.getLastName());
        assertEquals("220123456",   saved.getStudentNumber());
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void testFindById_ExistingId_ShouldReturnStudent() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        Optional<Student> result = studentRepository.findById(1L);
        assertTrue(result.isPresent());
        assertEquals("Jane", result.get().getFirstName());
    }

    @Test
    void testFindById_NonExistingId_ShouldReturnEmpty() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<Student> result = studentRepository.findById(99L);
        assertFalse(result.isPresent());
    }

    @Test
    void testFindAll_ShouldReturnList() {
        when(studentRepository.findAll()).thenReturn(List.of(student));
        List<Student> result = studentRepository.findAll();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testDeleteById_ShouldCallRepository() {
        studentRepository.deleteById(1L);
        verify(studentRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindByEmail_ExistingEmail_ShouldReturnStudent() {
        when(studentRepository.findByEmail("jane@cput.ac.za"))
                .thenReturn(Optional.of(student));
        Optional<Student> result = studentRepository.findByEmail("jane@cput.ac.za");
        assertTrue(result.isPresent());
        assertEquals("jane@cput.ac.za", result.get().getEmail());
    }

    @Test
    void testFindByEmail_NonExistingEmail_ShouldReturnEmpty() {
        when(studentRepository.findByEmail("unknown@cput.ac.za"))
                .thenReturn(Optional.empty());
        Optional<Student> result = studentRepository.findByEmail("unknown@cput.ac.za");
        assertFalse(result.isPresent());
    }

    @Test
    void testFindByStudentNumber_ShouldReturnStudent() {
        when(studentRepository.findByStudentNumber("220123456"))
                .thenReturn(Optional.of(student));
        Optional<Student> result = studentRepository.findByStudentNumber("220123456");
        assertTrue(result.isPresent());
        assertEquals("220123456", result.get().getStudentNumber());
    }

    @Test
    void testFindByFacultyId_ShouldReturnList() {
        when(studentRepository.findByFacultyId(1L))
                .thenReturn(List.of(student));
        List<Student> result = studentRepository.findByFacultyId(1L);
        assertNotNull(result);
        assertEquals(1, result.size());
    }




}
