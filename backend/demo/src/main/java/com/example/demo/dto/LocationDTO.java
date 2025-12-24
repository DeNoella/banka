package com.example.demo.dto;

import java.util.ArrayList;
import java.util.List;

public class LocationDTO {
    private String code;
    private String name;
    private String type;
    private String parentCode;
    private List<LocationDTO> children; // Include children as a list

    // Constructor for LocationDTO including children
    public LocationDTO(String code, String name, String type, String parentCode, List<LocationDTO> children) {
        this.code = code;
        this.name = name;
        this.type = type;
        this.parentCode = parentCode;
        this.children = (children != null) ? children : new ArrayList<>(); // Initialize with provided children or an
                                                                           // empty list
    }

    // Constructor without children for individual locations
    public LocationDTO(String code, String name, String type, String parentCode) {
        this(code, name, type, parentCode, new ArrayList<>()); // Call the other constructor to initialize children as
                                                               // an empty list
    }

    // Getters and Setters
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getParentCode() {
        return parentCode;
    }

    public List<LocationDTO> getChildren() {
        return children;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public void setChildren(List<LocationDTO> children) {
        this.children = children;
    }
}