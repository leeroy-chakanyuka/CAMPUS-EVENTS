package za.ac.cput.campus_events.factory;

import org.junit.jupiter.api.Test;
import za.ac.cput.campus_events.domain.Faculty;
import za.ac.cput.campus_events.domain.Student;

import static org.junit.jupiter.api.Assertions.*;

class StudentFactoryTest {

    private final Faculty faculty = new Faculty.Builder().build();

    @Test
    void createStudentSuccess() {
        Student student = StudentFactory.createStudent(
                faculty,
                "John",
                "Smith",
                "john.smith@gmail.com",
                "2201234567"
        );

        assertNotNull(student);
        assertEquals("John", student.getFirstName());
        assertEquals("Smith", student.getLastName());
        assertEquals("john.smith@gmail.com", student.getEmail());
        assertEquals("2201234567", student.getStudentNumber());
        assertEquals(faculty, student.getFaculty());
    }

    @Test
    void createStudentWithNullFirstName() {
        Student student = StudentFactory.createStudent(
                faculty,
                null,
                "Smith",
                "john.smith@gmail.com",
                "2201234567"
        );

        assertNull(student);
    }

    @Test
    void createStudentWithShortFirstName() {
        Student student = StudentFactory.createStudent(
                faculty,
                "Jo",
                "Smith",
                "john.smith@gmail.com",
                "2201234567"
        );

        assertNull(student);
    }

    @Test
    void createStudentWithNullLastName() {
        Student student = StudentFactory.createStudent(
                faculty,
                "John",
                null,
                "john.smith@gmail.com",
                "2201234567"
        );

        assertNull(student);
    }

    @Test
    void createStudentWithInvalidEmail() {
        Student student = StudentFactory.createStudent(
                faculty,
                "John",
                "Smith",
                "not-an-email",
                "2201234567"
        );

        assertNull(student);
    }

    @Test
    void createStudentWithNullFaculty() {
        Student student = StudentFactory.createStudent(
                null,
                "John",
                "Smith",
                "john.smith@gmail.com",
                "2201234567"
        );

        assertNull(student);
    }

    @Test
    void createStudentWithShortStudentNumber() {
        Student student = StudentFactory.createStudent(
                faculty,
                "John",
                "Smith",
                "john.smith@gmail.com",
                "12345"
        );

        assertNull(student);
    }
}