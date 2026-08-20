package za.ac.cput.campus_events.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "venues")
public class Venue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer capacity;

    @Embedded
    private Address address;

    protected Venue() {
    }

    private Venue(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.capacity = builder.capacity;
        this.address = builder.address;
    }

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public Integer getCapacity() { return capacity; }
    public Address getAddress() { return address; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
    public void setAddress(Address address) { this.address = address; }

    @Override
    public String toString() {
        return "Venue{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", capacity=" + capacity +
                ", address=" + address +
                '}';
    }

    //  Builder
    public static class Builder {
        private Long id;
        private String name;
        private Integer capacity;
        private Address address;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder capacity(Integer capacity) {
            this.capacity = capacity;
            return this;
        }

        public Builder address(Address address) {
            this.address = address;
            return this;
        }

        public Builder copy(Venue venue) {
            this.id = venue.id;
            this.name = venue.name;
            this.capacity = venue.capacity;
            this.address = venue.address;
            return this;
        }

        public Venue build() {
            return new Venue(this);
        }
    }
}

