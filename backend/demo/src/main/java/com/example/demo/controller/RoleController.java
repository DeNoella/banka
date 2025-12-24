package com.example.demo.controller;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Role;
import com.example.demo.service.RoleService;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping("/save")
    public ResponseEntity<?> createRole(@RequestBody Role role) {
        try {
            Role saved = roleService.createRole(role);
            return new ResponseEntity<>(" Role created successfully: " + saved.getRole_name(), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(" Error creating role: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRole(@PathVariable UUID id) {
        try {
            Optional<Role> role = roleService.getRoleById(id);

            if (role.isPresent()) {
                return new ResponseEntity<>(role.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Role not found", HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving role: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllRoles() {
        try {
            return ResponseEntity.ok(roleService.getAllRoles());
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving roles: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateRole(@PathVariable UUID id, @RequestBody Role role) {
        try {
            Role updated = roleService.updateRole(id, role);
            return ResponseEntity.ok("Role updated successfully: " + updated.getRole_name());
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating role: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable UUID id) {
        try {
            roleService.deleteRole(id);
            return ResponseEntity.ok("Role deleted successfully");
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting role: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
