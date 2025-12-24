package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "accountID")
    private UUID accountID;

    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private double balance;

    private boolean isVerifed = false;

    private boolean isActive = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type")
    private EAccount accountType;

    @JsonProperty("user")
    @Transient
    private UUID user_id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private BankUser user;

    @OneToOne(mappedBy = "account", fetch = FetchType.LAZY)
    private AccountDetails details;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Transaction> transactions = new ArrayList<>();

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Loan> loan = new ArrayList<>();

    public Account() {
    }

    public Account(UUID accountID, String accountNumber, double balance, boolean isVerifed, boolean isActive,
            EAccount accountType, BankUser user, AccountDetails details, List<Transaction> transactions,
            List<Loan> loan) {
        this.accountID = accountID;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.isVerifed = isVerifed;
        this.isActive = isActive;
        this.accountType = accountType;
        this.user = user;
        this.details = details;
        this.transactions = transactions;
        this.loan = loan;
    }

    public UUID getAccount_id() {
        return accountID;
    }

    public void setAccount_id(UUID accountID) {
        this.accountID = accountID;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public EAccount getAccountType() {
        return accountType;
    }

    public void setAccountType(EAccount accountType) {
        this.accountType = accountType;
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

    public void setVerified(boolean isVerifed) {
        this.isVerifed = isVerifed;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public EAccount getAccount_type() {
        return accountType;
    }

    public void setAccount_type(EAccount accountType) {
        this.accountType = accountType;
    }

    public BankUser getUser() {
        return user;
    }

    public void setUser(BankUser user) {
        this.user = user;
    }

    public AccountDetails getDetails() {
        return details;
    }

    public void setDetails(AccountDetails details) {
        this.details = details;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public List<Loan> getLoan() {
        return loan;
    }

    public void setLoan(List<Loan> loan) {
        this.loan = loan;
    }

}
