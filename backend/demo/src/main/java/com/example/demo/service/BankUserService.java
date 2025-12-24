package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.dto.BankUserDTO;
import com.example.demo.dto.CreateManagerDTO;
import com.example.demo.dto.LocationDTO;
import com.example.demo.model.BankUser;
import com.example.demo.model.ERole;
import com.example.demo.model.Location;
import com.example.demo.model.Role;
import com.example.demo.repository.BankUserRepository;
import com.example.demo.repository.LocationRepository;
import com.example.demo.repository.RoleRepository;

@Service
public class BankUserService {

    @Autowired
    private BankUserRepository bankUserRepository;

    @Autowired
    private LocationService locationService;

    @Autowired
    private LocationRepository locationRepository;

     @Autowired
    private RoleRepository roleRepository;

    // CREATE
    public String createUser(BankUser user) {
        if (bankUserRepository.existsByEmail(user.getEmail())) {
            return "User with this email already exists";
        }

        // Verify location exists
        if (user.getLocation() == null || user.getLocation().getLocationId() == null) {
            return "Location is required";
        }

        Location location = locationRepository.findById(user.getLocation().getLocationId())
                .orElse(null);
        if (location == null) {
            return "Invalid location ID";
        }

        user.setLocation(location);
        bankUserRepository.save(user);
        return "User created successfully";
    }

    // Convert BankUser to BankUserDTO with location hierarchy
    public BankUserDTO convertToDTO(BankUser user) {
        LocationDTO locationDTO = null;
        List<LocationDTO> hierarchy = null;

        if (user.getLocation() != null) {
            Location loc = user.getLocation();
            locationDTO = new LocationDTO(
                    loc.getCode(),
                    loc.getName(),
                    loc.getType() != null ? loc.getType().toString() : null,
                    loc.getParentCode());

            // Get full location hierarchy
            List<Location> hierarchyList = locationService.getLocationHierarchy(loc.getCode());
            hierarchy = hierarchyList.stream()
                    .map(l -> new LocationDTO(l.getCode(), l.getName(),
                            l.getType().toString(), l.getParentCode()))
                    .collect(Collectors.toList());
        }

        return new BankUserDTO(
                user.getUser_id(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getUser_Role() != null ? user.getUser_Role().toString() : null,
                locationDTO,
                hierarchy);
    }

    // READ
    public Optional<BankUser> getUserById(UUID id) {
        return bankUserRepository.findById(id);
    }

    // READ (by email)
    public Optional<BankUser> getUserByEmail(String email) {
        return bankUserRepository.findByEmail(email);
    }

    // READ (by first and last name)
    public List<BankUser> getUsersByName(String firstName, String lastName) {
        return bankUserRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    // READ (by location)
    public List<BankUser> getUsersByLocation(Location location) {
        return bankUserRepository.findByLocation(location);
    }

    // READ (pagination)
    public Page<BankUser> getAllUsers(Pageable pageable) {
        return bankUserRepository.findAll(pageable);
    }

    // SEARCH (pagination)
    public Page<BankUser> searchUsers(String search, Pageable pageable) {
        return bankUserRepository.searchUsers(search, pageable);
    }

    // UPDATE
    public BankUser updateUser(UUID id, BankUser updatedUser) {
        return bankUserRepository.findById(id).map(user -> {
            user.setFirstName(updatedUser.getFirstName());
            user.setLastName(updatedUser.getLastName());
            user.setEmail(updatedUser.getEmail());
            user.setPhoneNumber(updatedUser.getPhoneNumber());
            user.setPassword(updatedUser.getPassword());
            user.setLocation(updatedUser.getLocation());
            user.setRoleID(updatedUser.getRoleID());
            return bankUserRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

    // DELETE
    public void deleteUser(UUID id) {
        if (!bankUserRepository.existsById(id)) {
            throw new RuntimeException("User not found with id " + id);
        }
        bankUserRepository.deleteById(id);
    }

    // SEARCH HELPERS
    public List<BankUser> searchUsersByFirstNamePrefix(String prefix) {
        return bankUserRepository.findByFirstNameStartingWith(prefix);
    }

    public List<BankUser> searchUsersByLastNameSuffix(String suffix) {
        return bankUserRepository.findByLastNameEndingWith(suffix);
    }

    public ResponseEntity<?> createManager(CreateManagerDTO dto) {

        if (bankUserRepository.existsByEmail(dto.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        Role managerRole = roleRepository.findByRoleName(ERole.MANAGER)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        BankUser manager = new BankUser();
        manager.setFirstName(dto.getFirstName());
        manager.setLastName(dto.getLastName());
        manager.setEmail(dto.getEmail());
        manager.setPhoneNumber(dto.getPhoneNumber());
        manager.setPassword("manager123");
        manager.setUser_Role(ERole.MANAGER);
        manager.setRoleID(managerRole);

        bankUserRepository.save(manager);
        return ResponseEntity.ok("Manager created");
    }

}