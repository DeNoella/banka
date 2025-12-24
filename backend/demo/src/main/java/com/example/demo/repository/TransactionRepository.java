package com.example.demo.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.Account;
import com.example.demo.model.Transaction;

import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    List<Transaction> findByAccount(Account account);

    Page<Transaction> findByAccount(Account account, Pageable pageable);

    List<Transaction> findByAmountGreaterThan(Double amount);

    List<Transaction> findByAmountLessThan(Double amount);

    Page<Transaction> findAll(Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE " +
            "LOWER(t.account.accountNumber) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(t.account.user.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(t.account.user.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "CAST(t.amount AS string) LIKE CONCAT('%', :search, '%')")
    Page<Transaction> searchTransactions(@Param("search") String search, Pageable pageable);

    Page<Transaction> findByAccountIn(List<Account> accounts, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE t.account IN :accounts AND (" +
            "LOWER(t.account.accountNumber) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "CAST(t.amount AS string) LIKE CONCAT('%', :search, '%'))")
    Page<Transaction> searchTransactionsByAccounts(@Param("accounts") List<Account> accounts,
            @Param("search") String search, Pageable pageable);
}
