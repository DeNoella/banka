package com.example.demo.controller;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.LocationDTO;
import com.example.demo.model.ELocation;
import com.example.demo.model.Location;
import com.example.demo.service.LocationService;

@RestController
@RequestMapping(value = "/api/location")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @PostMapping(value = "/saveParentOrChild", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveLocation(
            @RequestParam(required = false) String parentCode,
            @RequestBody Location location) {

        String response = locationService.saveLocation(parentCode, location);

        if (response.equals("Child location saved successfully")) {
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else if (response.equals("Location with this code already exists")) {
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        } else if (response.contains("does not exist")) {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } else if (response.equals("Parent location saved successfully")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllLocations() {
        try {
            List<Location> locations = locationService.getAllLocations();

            if (locations.isEmpty()) {
                return new ResponseEntity<>("No locations found", HttpStatus.NOT_FOUND);
            }

            List<LocationDTO> locationDTOs = locations.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(locationDTOs);
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving locations: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/provinces")
    public ResponseEntity<?> getProvinces() {
        try {
            List<Location> provinces = locationService.getLocationsByType(ELocation.PROVINCE);

            if (provinces.isEmpty()) {
                return new ResponseEntity<>("No provinces found", HttpStatus.NOT_FOUND);
            }

            List<LocationDTO> provinceDTOs = provinces.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(provinceDTOs);
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving provinces: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/findByCode")
    public ResponseEntity<?> getLocationByCode(@RequestParam String code) {
        try {
            Optional<Location> location = locationService.getLocationByCode(code);

            if (location.isPresent()) {
                LocationDTO locationDTO = convertToDTOWithChildren(location.get());
                return ResponseEntity.ok(locationDTO);
            } else {
                return new ResponseEntity<>("Location with code '" + code + "' not found",
                        HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving location: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/hierarchy")
    public ResponseEntity<?> getLocationHierarchy(@RequestParam String villageCode) {
        try {
            List<Location> hierarchy = locationService.getLocationHierarchy(villageCode);

            if (hierarchy.isEmpty()) {
                return new ResponseEntity<>("Location with code '" + villageCode + "' not found",
                        HttpStatus.NOT_FOUND);
            }

            List<LocationDTO> hierarchyDTOs = hierarchy.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(hierarchyDTOs);
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving hierarchy: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/children")
    public ResponseEntity<?> getChildren(@RequestParam String code) {
        try {
            List<Location> children = locationService.getChildren(code);

            if (children.isEmpty()) {
                return new ResponseEntity<>("No children found for location with code '" + code + "'",
                        HttpStatus.NOT_FOUND);
            }

            List<LocationDTO> childrenDTOs = children.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(childrenDTOs);
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving children: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<?> deleteLocation(@RequestParam UUID id) {
        String result = locationService.deleteLocation(id);

        if (result.contains("successfully")) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/deleteByCode")
    public ResponseEntity<?> deleteLocationByCode(@RequestParam String code) {
        String result = locationService.deleteLocationByCode(code);

        if (result.contains("successfully")) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }
    }

    private LocationDTO convertToDTO(Location location) {
        return new LocationDTO(
                location.getCode(),
                location.getName(),
                location.getType().toString(),
                location.getParentCode());
    }

    private LocationDTO convertToDTOWithChildren(Location location) {
        List<LocationDTO> childrenDTOs = locationService.getChildren(location.getCode()).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new LocationDTO(
                location.getCode(),
                location.getName(),
                location.getType().toString(),
                location.getParentCode(),
                childrenDTOs);
    }
}