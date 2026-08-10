package za.ac.cput.campus_events.factory;
/*
Mologadi Dikgale
Student No: 231016263
 */

import za.ac.cput.campus_events.domain.Admin;
import za.ac.cput.campus_events.domain.Faculty;

public class FacultyFactory {

    public static Faculty createFaculty(String name, String status, String contactEmail, Admin createdByAdmin) {
        if (name == null || name.isEmpty()) return null;
        if (contactEmail == null || contactEmail.isEmpty()) return null;
        if (createdByAdmin == null) return null;

        return new Faculty.Builder()
                .setName(name)
                .setEmail(contactEmail)
                .setCreatedByAdmin(createdByAdmin)
                .build();
    }
}