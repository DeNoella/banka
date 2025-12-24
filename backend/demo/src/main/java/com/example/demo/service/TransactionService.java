package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.dto.TransactionDTO;
import com.example.demo.model.Account;
import com.example.demo.model.Transaction;
import com.example.demo.repository.TransactionRepository;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction createTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
        return transactionRepository.save(transaction);
    }

    public Optional<Transaction> getTransactionById(UUID id) {
        return transactionRepository.findById(id);
    }

    public Page<Transaction> getAllTransactions(Pageable pageable) {
        return transactionRepository.findAll(pageable);
    }

    public List<Transaction> getTransactionsByAccount(Account account) {
        return transactionRepository.findByAccount(account);
    }

    public List<Transaction> getTransactionsAboveAmount(Double amount) {
        return transactionRepository.findByAmountGreaterThan(amount);
    }

    public List<Transaction> getTransactionsBelowAmount(Double amount) {
        return transactionRepository.findByAmountLessThan(amount);
    }

    public Transaction updateTransaction(UUID id, Transaction updatedTransaction) {
        return transactionRepository.findById(id)
                .map(existing -> {
                    existing.setAmount(updatedTransaction.getAmount());
                    existing.setTimestamp(updatedTransaction.getTimestamp());
                    existing.setAccount(updatedTransaction.getAccount());
                    return transactionRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));
    }

    public void deleteTransaction(UUID id) {
        if (!transactionRepository.existsById(id)) {
            throw new RuntimeException("Transaction not found with id: " + id);
        }
        transactionRepository.deleteById(id);
    }

    public TransactionDTO convertToDTO(Transaction transaction) {
        Account account = transaction.getAccount();
        String userName = "N/A";

        if (account != null && account.getUser() != null) {
            userName = account.getUser().getFirstName() + " " + account.getUser().getLastName();
        }

        return new TransactionDTO(
                transaction.getTransaction_id(),
                transaction.getAmount(),
                transaction.getTimestamp(),
                account != null ? account.getAccount_id() : null,
                account != null ? account.getAccountNumber() : null,
                userName);
    }

    // Convert list to DTOs
    public List<TransactionDTO> convertToDTOList(List<Transaction> transactions) {
        return transactions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

}
