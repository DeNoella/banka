package com.example.demo.service;

import com.example.demo.model.Account;
import com.example.demo.model.AccountDetails;
import com.example.demo.repository.AccountDetailsRepository;
import com.example.demo.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AccountDetailsService {

    @Autowired
    private AccountDetailsRepository detailsRepository;

    @Autowired
    private AccountRepository accountRepository;

    public AccountDetails createDetails(AccountDetails details) {
        if (details.getAccount() != null && details.getAccount().getAccount_id() != null) {

            Account account = accountRepository.findById(details.getAccount().getAccount_id())
                    .orElseThrow(() -> new RuntimeException(
                            "Account not found with ID: " + details.getAccount().getAccount_id()));

            details.setAccount(account);
        } else {
            throw new RuntimeException("Account ID is required");
        }

        Optional<AccountDetails> existing = detailsRepository.findByAccount(details.getAccount());
        if (existing.isPresent()) {
            throw new RuntimeException("Account details already exist for this account");
        }

        return detailsRepository.save(details);
    }

    public List<AccountDetails> getAllDetails() {
        return detailsRepository.findAll();
    }

    public Optional<AccountDetails> getDetailsById(UUID id) {
        return detailsRepository.findById(id);
    }

    public List<AccountDetails> searchByCurrency(String currency) {
        return detailsRepository.findByCurrency(currency);
    }

    public List<AccountDetails> searchByBranchName(String namePart) {
        return detailsRepository.findByBranchNameContainingIgnoreCase(namePart);
    }

    public AccountDetails updateDetails(UUID id, AccountDetails updatedDetails) {
        return detailsRepository.findById(id)
                .map(existing -> {
                    existing.setBranchName(updatedDetails.getBranchName());
                    existing.setCurrency(updatedDetails.getCurrency());
                    existing.setCreatedAt(updatedDetails.getCreatedAt());

                    if (updatedDetails.getAccount() != null && updatedDetails.getAccount().getAccount_id() != null) {
                        Account account = accountRepository.findById(updatedDetails.getAccount().getAccount_id())
                                .orElseThrow(() -> new RuntimeException(
                                        "Account not found with ID: " + updatedDetails.getAccount().getAccount_id()));
                        existing.setAccount(account);
                    }

                    return detailsRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Account details not found with id: " + id));
    }

    // ✅ Delete details
    public void deleteDetails(UUID id) {
        if (!detailsRepository.existsById(id)) {
            throw new RuntimeException("Account details not found with id: " + id);
        }
        detailsRepository.deleteById(id);
    }
}
