package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.ERole;
import com.example.demo.model.Role;
import com.example.demo.repository.RoleRepository;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role createRole(Role role) {
        Optional<Role> existing = roleRepository.findByRoleName(role.getRole_name());
        if (existing.isPresent()) {
            throw new RuntimeException("Account with this name already exists.");
        }
        return roleRepository.save(role);
    }

    public Optional<Role> getRoleById(UUID id) {
        return roleRepository.findById(id);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Optional<Role> getRoleByName(ERole name) {
        return roleRepository.findByRoleName(name);
    }

    public List<Role> getRolesByNames(List<ERole> names) {
        return roleRepository.findByRoleNameIn(names);
    }

    public Role updateRole(UUID id, Role updatedRole) {
        return roleRepository.findById(id)
                .map(existing -> {
                    existing.setRole_name(updatedRole.getRole_name());
                    return roleRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));
    }

    public void deleteRole(UUID id) {
        if (!roleRepository.existsById(id)) {
            throw new RuntimeException("Role not found with id: " + id);
        }
        roleRepository.deleteById(id);
    }

}
