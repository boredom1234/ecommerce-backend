package com.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PlaceOrderRequest {
    private String shippingAddress;
    private Address address;
    
    public PlaceOrderRequest() {
    }
    
    public String getShippingAddress() {
        return shippingAddress;
    }
    
    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
    
    public Address getAddress() {
        return address;
    }
    
    public void setAddress(Address address) {
        this.address = address;
    }
    
    @JsonIgnore
    public String getFormattedShippingAddress() {
        if (shippingAddress != null && !shippingAddress.isEmpty()) {
            return shippingAddress;
        } else if (address != null) {
            return address.toString();
        }
        return null;
    }
    
    @Override
    public String toString() {
        return "PlaceOrderRequest{" +
                "shippingAddress='" + shippingAddress + '\'' +
                ", address=" + address +
                '}';
    }
} 