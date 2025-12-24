package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class TransactionDTO {
    private UUID transactionId;
    private Double amount;
    private LocalDateTime timestamp;
    private UUID accountId;
    private String accountNumber;
    private String userName;

    public TransactionDTO() {
    }

    public TransactionDTO(UUID transactionId, Double amount, LocalDateTime timestamp,
            UUID accountId, String accountNumber, String userName) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.timestamp = timestamp;
        this.accountId = accountId;
        this.accountNumber = accountNumber;
        this.userName = userName;
    }

    // Getters and Setters
    public UUID getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}