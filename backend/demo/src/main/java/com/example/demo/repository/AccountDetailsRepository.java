package com.example.demo.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Account;
import com.example.demo.model.AccountDetails;

@Repository
public interface AccountDetailsRepository extends JpaRepository<AccountDetails, UUID> {
    Optional<AccountDetails> findByAccount(Account account);

    List<AccountDetails> findByCurrency(String currency);

    List<AccountDetails> findByBranchNameContainingIgnoreCase(String branchName);
}
