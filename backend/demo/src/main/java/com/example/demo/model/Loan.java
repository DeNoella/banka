package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name = "loan")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "loanID")
    private UUID loan_id;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private Double interestRate;

    @Column(name = "start_Date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_Date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "isRepaid", nullable = false)
    private boolean isRepaid = false;

    @Enumerated(EnumType.STRING)
    private ELoan status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private BankUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    public Loan() {
    }

    public Loan(UUID loan_id, double amount, Double interestRate, LocalDateTime startDate, LocalDateTime endDate,
            boolean isRepaid, ELoan status, BankUser user, Account account) {
        this.loan_id = loan_id;
        this.amount = amount;
        this.interestRate = interestRate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isRepaid = isRepaid;
        this.status = status;
        this.user = user;
        this.account = account;
    }

    public UUID getLoan_id() {
        return loan_id;
    }

    public void setLoan_id(UUID loan_id) {
        this.loan_id = loan_id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public boolean isRepaid() {
        return isRepaid;
    }

    public void setRepaid(boolean isRepaid) {
        this.isRepaid = isRepaid;
    }

    public ELoan getStatus() {
        return status;
    }

    public void setStatus(ELoan status) {
        this.status = status;
    }

    public BankUser getUser() {
        return user;
    }

    public void setUser(BankUser user) {
        this.user = user;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

}
