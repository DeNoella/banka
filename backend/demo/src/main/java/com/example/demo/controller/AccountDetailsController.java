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

import com.example.demo.model.AccountDetails;
import com.example.demo.service.AccountDetailsService;

@RestController
@RequestMapping("/api/accountDetails")
public class AccountDetailsController {

    @Autowired
    private AccountDetailsService detailsService;

    @PostMapping("/save")
    public ResponseEntity<?> createDetails(@RequestBody AccountDetails details) {
        try {
            AccountDetails saved = detailsService.createDetails(details);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating details: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<AccountDetails>> getAllDetails() {
        List<AccountDetails> list = detailsService.getAllDetails();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDetailsById(@PathVariable UUID id) {
        return detailsService.getDetailsById(id)
                .<ResponseEntity<?>>map(details -> new ResponseEntity<>(details, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>("Account details not found", HttpStatus.NOT_FOUND));
    }

    @GetMapping("/currency/{currency}")
    public ResponseEntity<List<AccountDetails>> searchByCurrency(@PathVariable String currency) {
        return new ResponseEntity<>(detailsService.searchByCurrency(currency), HttpStatus.OK);
    }

    @GetMapping("/branch/{namePart}")
    public ResponseEntity<List<AccountDetails>> searchByBranchName(@PathVariable String namePart) {
        return new ResponseEntity<>(detailsService.searchByBranchName(namePart), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateDetails(@PathVariable UUID id, @RequestBody AccountDetails updated) {
        try {
            AccountDetails saved = detailsService.updateDetails(id, updated);
            return new ResponseEntity<>(saved, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating details: " + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteDetails(@PathVariable UUID id) {
        try {
            detailsService.deleteDetails(id);
            return new ResponseEntity<>("Account details deleted successfully.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting details: " + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
