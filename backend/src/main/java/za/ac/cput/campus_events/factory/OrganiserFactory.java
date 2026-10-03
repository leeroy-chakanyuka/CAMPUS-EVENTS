package za.ac.cput.campus_events.factory;
/*
Mologadi  Dikgale
Student No: 231016263
 */

import za.ac.cput.campus_events.domain.Organiser;
import za.ac.cput.campus_events.util.EmailValidator;

import java.time.LocalDateTime;

public class OrganiserFactory {


    public static Organiser createOrganiser(String firstName, String lastName, String email, String role) {
        //TODO : THESE CAN BE EXTRACTED INTO A METHOD SINCE ITS THE SAME CHECK FOR MOST THE STRINGS
        if(firstName == null || firstName.length() < 3){
            return null;
        }

        if(lastName == null || lastName.length() < 3){
            return null;
        }

        if(email == null || email.length() < 3){
            return null;
        }

        if(!EmailValidator.isValid(email)){
            // TODO: MAKE THE REGEX DETECT UNI EMAILS, IE : AC.ZA
            return null;
        }

        if(role == null || role.isBlank()){
            return null;
        }

        return new Organiser.Builder()
                .setFirstName(firstName)
                .setLastName(lastName)
                .setEmail(email)
                .setRole(role)
                .setCreatedAt(LocalDateTime.now())
                .build();
    }
}
