package com.example.demo.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.TransactionDTO;
import com.example.demo.model.Transaction;
import com.example.demo.service.TransactionService;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/save")
    public ResponseEntity<?> createTransaction(@RequestBody Transaction transaction) {
        try {
            Transaction saved = transactionService.createTransaction(transaction);
            return new ResponseEntity<>(" Transaction created with ID: " + saved.getTransaction_id(),
                    HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(" Error creating transaction: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTransaction(@PathVariable UUID id) {
        try {
            Optional<Transaction> transaction = transactionService.getTransactionById(id);

            if (transaction.isPresent()) {
                TransactionDTO dto = transactionService.convertToDTO(transaction.get());

                return ResponseEntity.ok(Map.of(
                        "message", "Transaction retrieved successfully",
                        "transaction", dto));
            } else {
                return new ResponseEntity<>(Map.of(
                        "message", " Transaction not found",
                        "transactionId", id), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of(
                    "message", " Error retrieving transaction",
                    "error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Transaction> transactionPage = transactionService.getAllTransactions(pageable);

            List<TransactionDTO> dtoList = transactionService.convertToDTOList(
                    transactionPage.getContent());

            return ResponseEntity.ok(Map.of(
                    "message", "Transactions retrieved successfully",
                    "transactions", dtoList,
                    "currentPage", page,
                    "totalPages", transactionPage.getTotalPages(),
                    "totalItems", transactionPage.getTotalElements()));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(Map.of(
                    "message", " Error retrieving transactions",
                    "error", e.getMessage(),
                    "errorType", e.getClass().getName()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateTransaction(@PathVariable UUID id, @RequestBody Transaction transaction) {
        try {
            Transaction updated = transactionService.updateTransaction(id, transaction);
            return ResponseEntity.ok(" Transaction updated successfully: " + updated.getTransaction_id());
        } catch (Exception e) {
            return new ResponseEntity<>(" Error updating transaction: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTransaction(@PathVariable UUID id) {
        try {
            transactionService.deleteTransaction(id);
            return ResponseEntity.ok("🗑️ Transaction deleted successfully");
        } catch (Exception e) {
            return new ResponseEntity<>(" Error deleting transaction: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
