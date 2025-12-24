package com.example.demo.dto;

import com.example.demo.model.ERole;

public class CreateUserDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private ERole role;
    
    public CreateUserDTO() {}
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
     public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public ERole getRole() {
        return role;
    }
    
    public void setRole(ERole role) {
        this.role = role;
    }

    private String locationCode;

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }
}
