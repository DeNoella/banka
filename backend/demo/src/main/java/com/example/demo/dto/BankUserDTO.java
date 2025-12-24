package com.example.demo.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.example.demo.model.Account;
import com.example.demo.model.Loan;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

public class BankUserDTO {
    private UUID userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String userRole;
    private LocationDTO location;
    private List<LocationDTO> locationHierarchy; // Full hierarchy: Province > District > Sector > Cell > Village

    public BankUserDTO() {
    }

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Account> accounts = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Loan> loans = new ArrayList<>();

    public BankUserDTO(UUID userId, String firstName, String lastName, String email, String phoneNumber,
            String userRole, LocationDTO location, List<LocationDTO> locationHierarchy) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userRole = userRole;
        this.location = location;
        this.locationHierarchy = locationHierarchy;
    }

    // Getters and Setters
    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

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

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public LocationDTO getLocation() {
        return location;
    }

    public void setLocation(LocationDTO location) {
        this.location = location;
    }

    public List<LocationDTO> getLocationHierarchy() {
        return locationHierarchy;
    }

    public void setLocationHierarchy(List<LocationDTO> locationHierarchy) {
        this.locationHierarchy = locationHierarchy;
    }
}