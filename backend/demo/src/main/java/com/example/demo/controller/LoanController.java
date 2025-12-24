package com.example.demo.controller;

import java.util.List;
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

import com.example.demo.model.Loan;
import com.example.demo.service.LoanService;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    @Autowired
    private LoanService service;

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody Loan loan) {
        Loan createdLoan = service.create(loan);
        return ResponseEntity.ok("Loan created successfully with ID: " + createdLoan.getLoan_id());
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        List<Loan> loans = service.getAll();
        if (loans.isEmpty()) {
            return ResponseEntity.ok("No loans found.");
        }
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/getLoan/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        Loan loan = service.getById(id);
        if (loan == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Loan not found with ID: " + id);
        }
        return ResponseEntity.ok(loan);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, @RequestBody Loan loan) {
        Loan updatedLoan = service.update(id, loan);
        return ResponseEntity.ok("Loan updated successfully with ID: " + updatedLoan.getLoan_id());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok("Loan deleted successfully with ID: " + id);
    }
}
