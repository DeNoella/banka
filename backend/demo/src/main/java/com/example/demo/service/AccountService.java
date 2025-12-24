package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.AccountResponseDTO;
import com.example.demo.dto.SimpleUserDTO;
import com.example.demo.model.Account;
import com.example.demo.model.BankUser;
import com.example.demo.model.EAccount;
import com.example.demo.repository.AccountRepository;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account createAccount(Account account) {
        Optional<Account> existing = accountRepository.findByAccountNumber(account.getAccountNumber());
        if (existing.isPresent()) {
            throw new RuntimeException("Account with this number already exists.");
        }

        return accountRepository.save(account);
    }

    // READ
    public Optional<Account> getAccountById(UUID id) {
        return accountRepository.findById(id);
    }

    public Optional<Account> getAccountByNumber(String number) {
        return accountRepository.findByAccountNumber(number);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public List<Account> getAccountsByUser(BankUser user) {
        return accountRepository.findByUser(user);
    }

    public List<Account> getAccountsByType(EAccount type) {
        return accountRepository.findByAccountType(type);
    }

    public List<Account> getActiveAccounts() {
        return accountRepository.findByIsActive(true);
    }

    public Account updateAccount(UUID id, Account updatedAccount) {
        return accountRepository.findById(id)
                .map(existing -> {
                    existing.setAccountNumber(updatedAccount.getAccountNumber());
                    existing.setBalance(updatedAccount.getBalance());
                    existing.setVerified(updatedAccount.isVerifed());
                    existing.setActive(updatedAccount.isActive());
                    existing.setAccount_type(updatedAccount.getAccount_type());
                    existing.setUser(updatedAccount.getUser());
                    return accountRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));
    }

    public void deleteAccount(UUID id) {
        if (!accountRepository.existsById(id)) {
            throw new RuntimeException("Account not found with id: " + id);
        }
        accountRepository.deleteById(id);
    }

    public AccountResponseDTO convertToDTO(Account account) {
        SimpleUserDTO userDTO = new SimpleUserDTO(
                account.getUser().getUser_id(),
                account.getUser().getFirstName(),
                account.getUser().getLastName(),
                account.getUser().getEmail(),
                account.getUser().getPhoneNumber());

        return new AccountResponseDTO(
                account.getAccount_id(),
                account.getAccountNumber(),
                account.getBalance(),
                account.isVerifed(),
                account.isActive(),
                account.getAccountType().name(),
                userDTO);
    }

    public List<AccountResponseDTO> convertToDTOList(List<Account> accounts) {
        return accounts.stream().map(account -> {
            SimpleUserDTO userDTO = new SimpleUserDTO(
                    account.getUser().getUser_id(),
                    account.getUser().getFirstName(),
                    account.getUser().getLastName(),
                    account.getUser().getEmail(),
                    account.getUser().getPhoneNumber());

            return new AccountResponseDTO(
                    account.getAccount_id(),
                    account.getAccountNumber(),
                    account.getBalance(),
                    account.isVerifed(),
                    account.isActive(),
                    account.getAccountType().name(),
                    userDTO);
        }).collect(Collectors.toList());
    }

}
