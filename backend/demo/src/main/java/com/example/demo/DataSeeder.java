package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.model.BankUser;
import com.example.demo.model.ERole;
import com.example.demo.model.Role;
import com.example.demo.repository.BankUserRepository;
import com.example.demo.repository.RoleRepository;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private BankUserRepository bankUserRepository;

    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // STEP 1: Seed roles first if they don't exist
        seedRoles();
        
        // STEP 2: Then seed admin user
        seedAdminUser();
    }
    
    private void seedRoles() {
        if (roleRepository.count() == 0) {
            System.out.println("🔧 Seeding roles...");
            
            // Create all roles
            Role adminRole = new Role();
            adminRole.setRole_name(ERole.ADMIN);
            roleRepository.save(adminRole);
            
            Role managerRole = new Role();
            managerRole.setRole_name(ERole.MANAGER);
            roleRepository.save(managerRole);
            
            Role cashierRole = new Role();
            cashierRole.setRole_name(ERole.CASHIER);
            roleRepository.save(cashierRole);
            
            Role clientRole = new Role();
            clientRole.setRole_name(ERole.CLIENT);
            roleRepository.save(clientRole);
            
            System.out.println("✅ Roles seeded successfully!");
        } else {
            System.out.println("ℹ️ Roles already exist, skipping role seeding.");
        }
    }
    
    private void seedAdminUser() {
        if (bankUserRepository.count() == 0) {
            System.out.println("🔧 Seeding admin user...");
            
            // Find admin role by role name instead of hardcoded UUID
            Role adminRole = roleRepository.findByRoleName(ERole.ADMIN)
                    .orElseThrow(() -> new RuntimeException("Admin role not found!"));

            BankUser admin = new BankUser();
            admin.setFirstName("Admin");
            admin.setLastName("Admin");
            admin.setEmail("mutesideno@gmail.com");
            admin.setPassword(passwordEncoder.encode("admin12345"));
            admin.setUser_Role(ERole.ADMIN);
            admin.setRoleID(adminRole);
            admin.setPhoneNumber("0712345678");
            
            bankUserRepository.save(admin);
            
            System.out.println("✅ Admin user created successfully!");
            System.out.println("📧 Email: mutesideno@gmail.com");
            System.out.println("🔑 Password: admin12345");
        } else {
            System.out.println("ℹ️ Admin user already exists, skipping seeding.");
        }
    }
}