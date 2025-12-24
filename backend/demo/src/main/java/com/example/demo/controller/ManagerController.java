package com.example.demo.controller;

import com.example.demo.dto.CreateUserDTO;
import com.example.demo.dto.BankUserDTO;
import com.example.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/manager")
@CrossOrigin(origins = "*")
public class ManagerController {

    @Autowired
    private AuthService authService;

    @Autowired
    private com.example.demo.repository.BankUserRepository bankUserRepository;

    @GetMapping("/stats")
    public ResponseEntity<?> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalClients", bankUserRepository.countByUser_Role(com.example.demo.model.ERole.CLIENT));
        stats.put("totalCashiers", bankUserRepository.countByUser_Role(com.example.demo.model.ERole.CASHIER));
        return ResponseEntity.ok(stats);
    }

    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody CreateUserDTO dto) {
        System.out.println("\n========== CREATE USER ENDPOINT HIT ==========");

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            System.out.println("Authentication: " + auth);
            System.out.println("Is Authenticated: " + (auth != null ? auth.isAuthenticated() : "NULL"));
            System.out.println("Principal: " + (auth != null ? auth.getPrincipal() : "NULL"));

            if (auth != null && auth.getAuthorities() != null) {
                System.out.println("Authorities: " + auth.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(", ")));
            }

            if (auth == null || !auth.isAuthenticated()) {
                System.out.println(" User is NOT authenticated");
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(createErrorResponse("You must be logged in to perform this action."));
            }

            boolean hasManagerRole = auth.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> {
                        String authority = grantedAuthority.getAuthority();
                        System.out.println("Checking authority: " + authority);
                        return authority.equals("ROLE_MANAGER");
                    });

            System.out.println("Has MANAGER role: " + hasManagerRole);

            if (!hasManagerRole) {
                System.out.println(" User does NOT have MANAGER role");
                Map<String, Object> errorResponse = createErrorResponse(
                        "Only MANAGER users can create cashiers and clients. Please login with manager credentials.");
                System.out.println("Returning error response: " + errorResponse);

                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body(errorResponse);
            }

            BankUserDTO user = authService.createUser(dto);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", dto.getRole().name() + " created successfully");
            response.put("data", user);

            System.out.println(" User created successfully");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println(" Exception occurred: " + e.getMessage());
            e.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("message", message);
        error.put("timestamp", System.currentTimeMillis());
        return error;
    }
}