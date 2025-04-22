package com.ecommerce.dto;

public class Address {
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    
    public Address() {
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (street != null && !street.isEmpty()) {
            sb.append(street).append(", ");
        }
        if (city != null && !city.isEmpty()) {
            sb.append(city).append(", ");
        }
        if (state != null && !state.isEmpty()) {
            sb.append(state).append(", ");
        }
        if (zipCode != null && !zipCode.isEmpty()) {
            sb.append(zipCode).append(", ");
        }
        if (country != null && !country.isEmpty()) {
            sb.append(country);
        }
        
        String result = sb.toString();
        // Remove trailing comma and space if present
        if (result.endsWith(", ")) {
            result = result.substring(0, result.length() - 2);
        }
        
        return result;
    }
} 