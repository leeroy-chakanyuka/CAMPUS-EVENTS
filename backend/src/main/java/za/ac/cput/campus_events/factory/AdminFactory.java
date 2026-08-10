package za.ac.cput.campus_events.factory;

import za.ac.cput.campus_events.domain.Admin;
import za.ac.cput.campus_events.util.EmailValidator;

public class AdminFactory {
    // TODO: SET PASSWORD
    public static Admin createAdmin(String firstName, String lastName,
                                        String email, String password){

        if(firstName == null || firstName.length() < 3){
            return null;
        }

        if(lastName == null || lastName.length() < 3){
            return null;
        }

        if(!EmailValidator.isValid(email)){
            return null;
        }

        return new Admin.Builder()
                .setFirstName(firstName)
                .setEmail(email)
                .setLastName(lastName)
                .setPassword(password)
                .build();
    }
}
