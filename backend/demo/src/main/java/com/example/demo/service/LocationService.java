package com.example.demo.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.ELocation;
import com.example.demo.model.Location;
import com.example.demo.repository.LocationRepository;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    // Save Location with optional parent code
    public String saveLocation(String parentCode, Location location) {
        // If parentCode is provided, this is a child location
        if (parentCode != null) {
            Optional<Location> getParent = locationRepository.findByCode(parentCode);

            if (getParent.isPresent()) {
                // Set the parentCode directly
                location.setParentCode(getParent.get().getCode());

                // Check if location with this code already exists
                if (!locationRepository.existsByCode(location.getCode())) {
                    locationRepository.save(location);
                    return "Child location saved successfully";
                } else {
                    return "Location with this code already exists";
                }
            } else {
                return "Parent location with code '" + parentCode + "' does not exist";
            }
        } else {
            // If no parentCode, this is a root location (Province)
            if (!locationRepository.existsByCode(location.getCode())) {
                locationRepository.save(location);
                return "Parent location saved successfully";
            } else {
                return "Location with this code already exists";
            }
        }
    }

    // Get Location by ID
    public Optional<Location> getLocationById(UUID id) {
        return locationRepository.findById(id);
    }

    // Get Location by Code
    public Optional<Location> getLocationByCode(String code) {
        return locationRepository.findByCode(code);
    }

    // Get all Locations
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    // Get Locations by Type (e.g., get all provinces)
    public List<Location> getLocationsByType(ELocation type) {
        return locationRepository.findByType(type);
    }

    // Get Locations by Parent (e.g., get all districts of a province)
    public List<Location> getChildren(String parent) {
        return locationRepository.findByParentCode(parent);
    }

    // Get full hierarchy from village up to province
    public List<Location> getLocationHierarchy(String villageCode) {
        List<Location> hierarchy = new ArrayList<>();

        Optional<Location> currentLocation = locationRepository.findByCode(villageCode);

        if (currentLocation.isEmpty()) {
            return hierarchy; // Return empty list if village not found
        }

        Location location = currentLocation.get();

        // Add current location and traverse up to parent
        while (location != null) {
            hierarchy.add(location);

            // Retrieve the parent location using the parentCode
            String parentCode = location.getParentCode();

            // If parentCode is null or empty, break the loop
            if (parentCode == null || parentCode.isEmpty()) {
                break;
            }

            // Find the parent location in the repository
            Optional<Location> parentLocationOptional = locationRepository.findByCode(parentCode);

            // Assign the parent location or set to null if not found
            location = parentLocationOptional.orElse(null);
        }

        // Reverse to get Province > District > Sector > Cell > Village order
        Collections.reverse(hierarchy);

        return hierarchy;
    }

    // Delete Location by ID
    public String deleteLocation(UUID id) {
        if (locationRepository.existsById(id)) {
            locationRepository.deleteById(id);
            return "Location deleted successfully";
        } else {
            return "Location not found";
        }
    }

    // Delete Location by Code
    public String deleteLocationByCode(String code) {
        Optional<Location> location = locationRepository.findByCode(code);

        if (location.isPresent()) {
            locationRepository.delete(location.get());
            return "Location deleted successfully";
        } else {
            return "Location with code '" + code + "' not found";
        }
    }
}