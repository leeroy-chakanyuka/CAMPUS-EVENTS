package za.ac.cput;

// Temporary stub so Tony's MyEventsPanel compiles.
// MyEventsPanel expects a combo-box wrapper with id + name.
public class VenueItem {

    private final Long id;
    private final String name;

    public VenueItem(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
