package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name = "role")
public class Role {
     @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "roleID")
    private UUID roleId;

    @Column(name = "roleName")
    @Enumerated(EnumType.STRING)
    private ERole roleName;

    @OneToMany(mappedBy = "roleID")
    private List<BankUser> users = new ArrayList<>();

    public Role() {
    }

    public Role(UUID roleId, ERole roleName, List<BankUser> users) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.users = users;
    }
    public UUID getRole_id() {
        return roleId;
    }

    public void setRole_id(UUID roleId) {
        this.roleId = roleId;
    }

    public ERole getRole_name() {
        return roleName;
    }

    public void setRole_name(ERole roleName) {
        this.roleName = roleName;
    }

    public List<BankUser> getUsers() {
        return users;
    }

    public void setUsers(List<BankUser> users) {
        this.users = users;
    }

}
