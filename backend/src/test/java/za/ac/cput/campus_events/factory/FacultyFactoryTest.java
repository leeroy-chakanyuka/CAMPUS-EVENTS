package za.ac.cput.campus_events.factory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.ac.cput.campus_events.domain.Admin;
import za.ac.cput.campus_events.domain.Faculty;

import static org.junit.jupiter.api.Assertions.*;

class FacultyFactoryTest {

    private FacultyFactory facultyFactory;
    private Admin admin;

    @BeforeEach
    void setUp() {
        facultyFactory = new FacultyFactory();
        admin = new Admin.Builder()
                .setFirstName("John")
                .setLastName("Doe")
                .setEmail("john@cput.ac.za")
                .build();
    }

//    @Test
//    void testCreateFaculty_ValidInputs_ShouldReturnFaculty() {
//        Faculty faculty = facultyFactory.createFaculty(
//                "Faculty of Engineering",
//                "engineering@cput.ac.za",
//                admin
//        );
//
//        assertNotNull(faculty);
//        assertEquals("Faculty of Engineering", faculty.getName());
//        assertEquals("engineering@cput.ac.za", faculty.getContactEmail());
//        assertEquals(admin, faculty.getCreatedByAdmin());
//    }

    @Test
    void testCreateFaculty_NullName_ShouldReturnNull() {
        Faculty faculty = facultyFactory.createFaculty(
                null,
                "ACTIVE",
                "engineering@cput.ac.za",
                admin
        );

        assertNull(faculty);
    }

    @Test
    void testCreateFaculty_EmptyName_ShouldReturnNull() {
        Faculty faculty = facultyFactory.createFaculty(
                "",
                "ACTIVE",
                "engineering@cput.ac.za",
                admin
        );

        assertNull(faculty);
    }

    @Test
    void testCreateFaculty_NullStatus_ShouldReturnNull() {
        Faculty faculty = facultyFactory.createFaculty(
                "Faculty of Engineering",
                null,
                "engineering@cput.ac.za",
                admin
        );

        assertNull(faculty);
    }

    @Test
    void testCreateFaculty_EmptyStatus_ShouldReturnNull() {
        Faculty faculty = facultyFactory.createFaculty(
                "Faculty of Engineering",
                "",
                "engineering@cput.ac.za",
                admin
        );

        assertNull(faculty);
    }

    @Test
    void testCreateFaculty_InvalidEmail_ShouldReturnNull() {
        Faculty faculty = facultyFactory.createFaculty(
                "Faculty of Engineering",
                "ACTIVE",
                "invalidemail",
                admin
        );

        assertNull(faculty);
    }

    @Test
    void testCreateFaculty_NullEmail_ShouldReturnNull() {
        Faculty faculty = facultyFactory.createFaculty(
                "Faculty of Engineering",
                "ACTIVE",
                null,
                admin
        );

        assertNull(faculty);
    }

    @Test
    void testCreateFaculty_NullAdmin_ShouldReturnNull() {
        Faculty faculty = facultyFactory.createFaculty(
                "Faculty of Engineering",
                "ACTIVE",
                "engineering@cput.ac.za",
                null
        );

        assertNull(faculty);
    }

    @Test
    void testCreateFaculty_CreatedAtShouldBeSetAutomatically() {
        Faculty faculty = facultyFactory.createFaculty(
                "Faculty of Engineering",
                "ACTIVE",
                "engineering@cput.ac.za",
                admin
        );

        assertNotNull(faculty);
        assertNotNull(faculty.getCreatedAt());
    }
}