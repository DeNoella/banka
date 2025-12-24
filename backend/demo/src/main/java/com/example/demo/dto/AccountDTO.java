package com.example.demo.dto;

import java.util.UUID;

public class AccountDTO {
    private String accountNumber;
    private double balance;
    private boolean isVerifed;
    private boolean isActive;
    private String accountType;
    private UUID userId;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean isVerifed() {
        return isVerifed;
    }

    public void setVerifed(boolean isVerifed) {
        this.isVerifed = isVerifed;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
