package com.example.demo.dto;

import java.util.UUID;

public class AccountResponseDTO {
    private UUID accountId;
    private String accountNumber;
    private double balance;
    private boolean isVerified;
    private boolean isActive;
    private String accountType;
    private SimpleUserDTO user;

    public AccountResponseDTO() {
    }

    public AccountResponseDTO(UUID accountId, String accountNumber, double balance,
            boolean isVerified, boolean isActive, String accountType,
            SimpleUserDTO user) {
        this.accountId = accountId;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.isVerified = isVerified;
        this.isActive = isActive;
        this.accountType = accountType;
        this.user = user;
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

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public SimpleUserDTO getUser() {
        return user;
    }

    public void setUser(SimpleUserDTO user) {
        this.user = user;
    }
}
