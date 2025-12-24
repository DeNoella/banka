package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name = "accountDetails")
public class AccountDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "accountDetailsID", nullable = false)
    private UUID accountDetails_id;

    @Column(nullable = false)
    private String branchName;

    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String currency;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "accountID")
    private Account account;

    public AccountDetails() {
    }

    public AccountDetails(UUID accountDetails_id, String branchName, LocalDateTime createdAt, String currency,
            Account account) {
        this.accountDetails_id = accountDetails_id;
        this.branchName = branchName;
        this.createdAt = createdAt;
        this.currency = currency;
        this.account = account;
    }

    public UUID getAccountDetails_id() {
        return accountDetails_id;
    }

    public void setAccountDetails_id(UUID accountDetails_id) {
        this.accountDetails_id = accountDetails_id;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

}
