package com.example.demo.repository;

import java.util.*;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.Account;
import com.example.demo.model.BankUser;
import com.example.demo.model.EAccount;

import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    List<Account> findByUser(BankUser user);

    Page<Account> findByUser(BankUser user, Pageable pageable);

    List<Account> findByAccountType(EAccount type);

    List<Account> findByIsActive(boolean active);

    List<Account> findByIsVerifed(boolean verified);

    Optional<Account> findByAccountNumber(String accountNumber);

    Page<Account> findAll(Pageable pageable);

    @Query("SELECT a FROM Account a WHERE " +
            "LOWER(a.accountNumber) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(CAST(a.accountType AS string)) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(a.user.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(a.user.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(a.user.email) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Account> searchAccounts(@Param("search") String search, Pageable pageable);

    @Query("SELECT a FROM Account a WHERE a.user = :user AND (" +
            "LOWER(a.accountNumber) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(CAST(a.accountType AS string)) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Account> searchAccountsByUser(@Param("user") BankUser user, @Param("search") String search, Pageable pageable);
}
