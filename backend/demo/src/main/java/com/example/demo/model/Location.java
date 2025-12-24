package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

@Entity
@Table(name = "location")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "locationID")
    private UUID locationId;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(name = "locationName", nullable = false)
    private String name;

    @Column(name = "parent_code")
    private String parentCode;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "parentLocation")
    @JsonManagedReference
    private List<Location> children = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ELocation type;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "location")
    @JsonIgnore
    private List<BankUser> users = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_code", referencedColumnName = "code", insertable = false, updatable = false)
    @JsonBackReference
    private Location parentLocation;

    public Location() {
    }

    public Location(UUID locationId, String code, String name, String parentCode, List<Location> children,
            ELocation type, List<BankUser> users) {
        this.locationId = locationId;
        this.code = code;
        this.name = name;
        this.parentCode = parentCode;
        this.children = children;
        this.type = type;
        this.users = users;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public List<BankUser> getUsers() {
        return users;
    }

    public void setUsers(List<BankUser> users) {
        this.users = users;
    }

    public List<Location> getChildren() {
        return new ArrayList<>();
    }

    public void setChildren(List<Location> children) {
        this.children = children;
    }

    public ELocation getType() {
        return type;
    }

    public void setType(ELocation type) {
        this.type = type;
    }

    public List<BankUser> getUser() {
        return users;
    }

    public void setUser(List<BankUser> user) {
        this.users = user;
    }

    public UUID getLocationId() {
        return locationId;
    }

    public void setLocationId(UUID locationId) {
        this.locationId = locationId;
    }
}
