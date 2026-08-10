package za.ac.cput.campus_events.factory;

import za.ac.cput.campus_events.domain.Address;
import za.ac.cput.campus_events.domain.Venue;

public class VenueFactory {

    public static Venue createVenue(String name, Integer capacity, Address address) {


        if (name == null || name.trim().length() < 3) {
            return null;
        }

        if (capacity == null || capacity <= 0 || capacity > 1500) {
            return null;
        }

        if (address == null) {
            return null;
        }

        return new Venue.Builder()
                .setName(name.trim())
                .setCapacity(capacity)
                .setAddress(address)
                .build();
    }
}
