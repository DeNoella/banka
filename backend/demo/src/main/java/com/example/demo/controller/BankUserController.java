package com.example.demo.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.BankUserDTO;
import com.example.demo.model.BankUser;
import com.example.demo.model.Location;
import com.example.demo.service.BankUserService;
import com.example.demo.service.LocationService;

@RestController
@RequestMapping("/api/users")
public class BankUserController {

    @Autowired
    private BankUserService bankUserService;

    @Autowired
    private LocationService locationService;

    // CREATE - Save new user
    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveUser(@RequestBody BankUser user) {
        try {
            String response = bankUserService.createUser(user);
            if (response.equals("User created successfully")) {
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } else if (response.equals("User with this email already exists")) {
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            } else {
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating user: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable UUID id) {
        try {
            Optional<BankUser> user = bankUserService.getUserById(id);
            if (user.isPresent()) {
                BankUserDTO dto = bankUserService.convertToDTO(user.get());
                return ResponseEntity.ok(dto);
            } else {
                return new ResponseEntity<>("User not found with id: " + id, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving user: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get user by email
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getUserByEmail(@RequestParam String email) {
        try {
            Optional<BankUser> user = bankUserService.getUserByEmail(email);
            if (user.isPresent()) {
                BankUserDTO dto = bankUserService.convertToDTO(user.get());
                return ResponseEntity.ok(dto);
            } else {
                return new ResponseEntity<>("User not found with email: " + email, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving user: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get all users (with pagination)
    @GetMapping("/all")
public ResponseEntity<?> getAllUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
    try {
        Pageable pageable = PageRequest.of(page, size);
        Page<BankUser> users = bankUserService.getAllUsers(pageable);

        // Convert to DTO list
        List<BankUserDTO> userDTOs = users
                .stream()
                .map(bankUserService::convertToDTO)
                .toList();

        return ResponseEntity.ok(userDTOs);
    } catch (Exception e) {
        return new ResponseEntity<>("Error retrieving users: " + e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}


    // READ - Get users by name
    @GetMapping("/name")
    public ResponseEntity<?> getUsersByName(
            @RequestParam String firstName,
            @RequestParam String lastName) {
        try {
            List<BankUser> users = bankUserService.getUsersByName(firstName, lastName);
            if (users.isEmpty()) {
                return new ResponseEntity<>("No users found with name: " + firstName + " " + lastName,
                        HttpStatus.NOT_FOUND);
            }

            List<BankUserDTO> userDTOs = users.stream()
                    .map(user -> bankUserService.convertToDTO(user))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(userDTOs);
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving users: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get users by location
    @GetMapping("/location")
    public ResponseEntity<?> getUsersByLocation(@RequestParam String locationCode) {
        try {
            Optional<Location> location = locationService.getLocationByCode(locationCode);
            if (location.isEmpty()) {
                return new ResponseEntity<>("Location not found with code: " + locationCode,
                        HttpStatus.NOT_FOUND);
            }

            List<BankUser> users = bankUserService.getUsersByLocation(location.get());
            if (users.isEmpty()) {
                return new ResponseEntity<>("No users found for location: " + locationCode,
                        HttpStatus.NOT_FOUND);
            }

            List<BankUserDTO> userDTOs = users.stream()
                    .map(user -> bankUserService.convertToDTO(user))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(userDTOs);
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving users: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Search users by first name prefix
    @GetMapping("/search/firstname")
    public ResponseEntity<?> searchByFirstName(@RequestParam String prefix) {
        try {
            List<BankUser> users = bankUserService.searchUsersByFirstNamePrefix(prefix);
            if (users.isEmpty()) {
                return new ResponseEntity<>("No users found with first name starting with: " + prefix,
                        HttpStatus.NOT_FOUND);
            }

            List<BankUserDTO> userDTOs = users.stream()
                    .map(user -> bankUserService.convertToDTO(user))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(userDTOs);
        } catch (Exception e) {
            return new ResponseEntity<>("Error searching users: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Search users by last name suffix
    @GetMapping("/search/lastname")
    public ResponseEntity<?> searchByLastName(@RequestParam String suffix) {
        try {
            List<BankUser> users = bankUserService.searchUsersByLastNameSuffix(suffix);
            if (users.isEmpty()) {
                return new ResponseEntity<>("No users found with last name ending with: " + suffix,
                        HttpStatus.NOT_FOUND);
            }

            List<BankUserDTO> userDTOs = users.stream()
                    .map(user -> bankUserService.convertToDTO(user))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(userDTOs);
        } catch (Exception e) {
            return new ResponseEntity<>("Error searching users: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Update user
    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateUser(@PathVariable UUID id, @RequestBody BankUser user) {
        try {
            BankUser updatedUser = bankUserService.updateUser(id, user);
            BankUserDTO dto = bankUserService.convertToDTO(updatedUser);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating user: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // DELETE - Delete user
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {
        try {
            bankUserService.deleteUser(id);
            return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting user: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}