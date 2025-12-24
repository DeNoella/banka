package com.example.demo.controller;

import java.util.*;

import com.example.demo.dto.AccountDTO;
import com.example.demo.dto.AccountResponseDTO;
import com.example.demo.model.Account;
import com.example.demo.model.BankUser;
import com.example.demo.model.EAccount;
import com.example.demo.repository.BankUserRepository;
import com.example.demo.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private BankUserRepository bankUserRepository;

    @PostMapping("/save")
    public ResponseEntity<?> createAccount(@RequestBody AccountDTO dto) {
        try {
            Account account = new Account();
            account.setAccountNumber(dto.getAccountNumber());
            account.setBalance(dto.getBalance());
            account.setVerified(dto.isVerifed());
            account.setActive(dto.isActive());
            account.setAccount_type(EAccount.valueOf(dto.getAccountType()));

            // Resolve BankUser from UUID
            BankUser user = bankUserRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getUserId()));
            account.setUser(user);

            Account saved = accountService.createAccount(account);

            return new ResponseEntity<>("Account created successfully with ID: " + saved.getAccount_id(),
                    HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating account: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAccount/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable UUID id) {
        try {
            Optional<Account> accountOpt = accountService.getAccountById(id);

            if (accountOpt.isPresent()) {
                AccountResponseDTO dto = accountService.convertToDTO(accountOpt.get());
                return new ResponseEntity<>(dto, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Account not found with id: " + id, HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving account: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllAccounts() {
        try {
            List<Account> accounts = accountService.getAllAccounts();

            if (accounts.isEmpty()) {
                return new ResponseEntity<>("No accounts found.", HttpStatus.NOT_FOUND);
            }

            List<AccountResponseDTO> dtoList = accountService.convertToDTOList(accounts);
            return new ResponseEntity<>(dtoList, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving accounts: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateAccount(@PathVariable UUID id, @RequestBody Account account) {
        try {
            Account updated = accountService.updateAccount(id, account);
            AccountResponseDTO dto = accountService.convertToDTO(updated);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Account not found: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating account: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable UUID id) {
        try {
            Optional<Account> accountOpt = accountService.getAccountById(id);
            if (accountOpt.isEmpty()) {
                return new ResponseEntity<>("Account not found", HttpStatus.NOT_FOUND);
            }

            Account deleted = accountOpt.get();
            AccountResponseDTO dto = accountService.convertToDTO(deleted);
            accountService.deleteAccount(id);

            return ResponseEntity.ok(Map.of(
                    "message", "🗑️ Account deleted successfully",
                    "deletedAccount", dto));
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Account not found: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting account: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
