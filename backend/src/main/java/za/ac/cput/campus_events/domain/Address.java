package za.ac.cput.campus_events.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public class Address {
    private String street;
    private String suburb;
    private String city;
    private String postalCode;
    private String province;

    public Address(Builder builder) {
        this.city = builder.city;
        this.street = builder.street;
        this.suburb = builder.suburb;
        this.postalCode = builder.postalCode;
        this.province = builder.postalCode;
    }
    public Address(){}


    public String getStreet() {
        return street;
    }

    public String getSuburb() {
        return suburb;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getProvince() {
        return province;
    }

    public static class Builder{
        private String street;
        private String suburb;
        private String city;
        private String postalCode;
        private String province;

        public Builder setSuburb(String suburb){
            this.suburb = suburb;
            return this;
        }

        public Builder setCity(String city){
            this.city = city;
            return this;
        }

        public Builder setPostalCode(String street){
            this.street = street;
            return this;
        }

        public Builder setStreet(String street){
            this.street = street;
            return this;
        }

        public Builder setProvince(String province){
            this.province = province;
            return this;
        }

        public Address build(){
            return new Address(this);
        }
    }

    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                ", suburb='" + suburb + '\'' +
                ", city='" + city + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", province='" + province + '\'' +
                '}';
    }
}
