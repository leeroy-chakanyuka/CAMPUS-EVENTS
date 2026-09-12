package za.ac.cput.campus_events.factory;

import org.junit.jupiter.api.Test;
import za.ac.cput.campus_events.domain.Admin;

import static org.junit.jupiter.api.Assertions.*;

class AdminFactoryTest {@Test
void createAdminSuccess() {
    Admin admin = AdminFactory.createAdmin(
            "Alice",
            "Jones",
            "alice.jones@gmail.com",
            "Password123!"
    );

    assertNotNull(admin);
}

    @Test
    void createAdminWithNullFirstName() {
        Admin admin = AdminFactory.createAdmin(
                null,
                "Jones",
                "alice.jones@gmail.com",
                "Password123!"
        );

        assertNull(admin);
    }

    @Test
    void createAdminWithShortFirstName() {
        Admin admin = AdminFactory.createAdmin(
                "Al",
                "Jones",
                "alice.jones@gmail.com",
                "Password123!"
        );

        assertNull(admin);
    }

    @Test
    void createAdminWithNullLastName() {
        Admin admin = AdminFactory.createAdmin(
                "Alice",
                null,
                "alice.jones@gmail.com",
                "Password123!"
        );

        assertNull(admin);
    }

    @Test
    void createAdminWithShortLastName() {
        Admin admin = AdminFactory.createAdmin(
                "Alice",
                "Jo",
                "alice.jones@gmail.com",
                "Password123!"
        );

        assertNull(admin);
    }

    @Test
    void createAdminWithInvalidEmail() {
        Admin admin = AdminFactory.createAdmin(
                "Alice",
                "Jones",
                "invalid-email",
                "Password123!"
        );

        assertNull(admin);
    }
}