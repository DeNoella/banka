package com.example.demo.controller;

import com.example.demo.dto.AccountResponseDTO;
import com.example.demo.dto.CreateManagerDTO;
import com.example.demo.dto.LoanDTO;
import com.example.demo.dto.UpdateUserDTO;
import com.example.demo.model.Account;
import com.example.demo.model.BankUser;
import com.example.demo.dto.BankUserDTO;
import com.example.demo.model.Loan;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.LoanRepository;
import com.example.demo.service.AccountService;
import com.example.demo.service.AuthService;
import com.example.demo.service.BankUserService;
import com.example.demo.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = { "Authorization", "Content-Type",
        "Accept" }, allowCredentials = "true")
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AuthService authService;

    @Autowired
    private BankUserService bankUserService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private LoanService loanService;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private com.example.demo.repository.BankUserRepository bankUserRepository;

    @PostMapping("/create-manager")
    public ResponseEntity<?> createManager(@RequestBody CreateManagerDTO dto) {

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

            boolean hasAdminRole = auth.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> {
                        String authority = grantedAuthority.getAuthority();
                        System.out.println("Checking authority: " + authority);
                        return authority.equals("ROLE_ADMIN");
                    });

            System.out.println("Has ADMIN role: " + hasAdminRole);

            if (!hasAdminRole) {
                System.out.println(" User does NOT have ADMIN role");
                Map<String, Object> errorResponse = createErrorResponse(
                        "Only ADMIN users can create managers. Please login with admin credentials.");
                System.out.println("Returning error response: " + errorResponse);

                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body(errorResponse);
            }

            System.out.println(" User has ADMIN role, proceeding to create manager");
            BankUserDTO manager = authService.createManager(dto);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Manager created successfully");
            response.put("data", manager);

            System.out.println(" Manager created successfully");
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

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String search) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<BankUser> usersPage;

            if (search != null && !search.trim().isEmpty()) {
                usersPage = bankUserService.searchUsers(search, pageable);
            } else {
                usersPage = bankUserService.getAllUsers(pageable);
            }

            Page<BankUserDTO> dtoPage = usersPage.map(bankUserService::convertToDTO);

            return ResponseEntity.ok(dtoPage);
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving users: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable UUID id, @RequestBody UpdateUserDTO dto) {
        try {
            BankUser existingUser = bankUserService.getUserById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Update fields
            if (dto.getFirstName() != null) {
                existingUser.setFirstName(dto.getFirstName());
            }
            if (dto.getLastName() != null) {
                existingUser.setLastName(dto.getLastName());
            }
            if (dto.getEmail() != null && !dto.getEmail().equals(existingUser.getEmail())) {
                if (bankUserService.getUserByEmail(dto.getEmail()).isPresent()) {
                    return ResponseEntity.badRequest().body(createErrorResponse("Email already exists"));
                }
                existingUser.setEmail(dto.getEmail());
            }
            if (dto.getPhoneNumber() != null) {
                existingUser.setPhoneNumber(dto.getPhoneNumber());
            }

            BankUser updatedUser = bankUserService.updateUser(id, existingUser);
            BankUserDTO userDTO = bankUserService.convertToDTO(updatedUser);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "User updated successfully");
            response.put("data", userDTO);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> globalSearch(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);

            // Search users
            Page<BankUser> users = bankUserService.searchUsers(q, pageable);

            // Search accounts
            Page<Account> accounts = accountRepository.searchAccounts(q, pageable);

            // Search loans
            Page<Loan> loans = loanRepository.searchLoans(q, pageable);

            // Search transactions (if needed)
            // Note: Transaction search might need a separate repository method

            // Prepare response
            Map<String, Object> results = new HashMap<>();
            results.put("users", users.map(bankUserService::convertToDTO));
            results.put("accounts", accounts.map(accountService::convertToDTO));
            results.put("loans", loans.map(loanService::convertToDTO));
            results.put("query", q);

            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return new ResponseEntity<>("Error performing search: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/accounts")
    public ResponseEntity<?> getAllAccounts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String search) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Account> accountsPage;

            if (search != null && !search.trim().isEmpty()) {
                accountsPage = accountRepository.searchAccounts(search, pageable);
            } else {
                accountsPage = accountRepository.findAll(pageable);
            }

            Page<AccountResponseDTO> dtoPage = accountsPage.map(accountService::convertToDTO);
            return ResponseEntity.ok(dtoPage);
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving accounts: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/loans")
    public ResponseEntity<?> getAllLoans(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String search) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Loan> loansPage;

            if (search != null && !search.trim().isEmpty()) {
                loansPage = loanRepository.searchLoans(search, pageable);
            } else {
                loansPage = loanRepository.findAll(pageable);
            }

            Page<LoanDTO> dtoPage = loansPage.map(loanService::convertToDTO);
            return ResponseEntity.ok(dtoPage);
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving loans: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", bankUserRepository.count());
        stats.put("totalAccounts", accountRepository.count());
        stats.put("activeLoans", loanRepository.countByStatus(com.example.demo.model.ELoan.APPROVED));
        return ResponseEntity.ok(stats);
    }

}