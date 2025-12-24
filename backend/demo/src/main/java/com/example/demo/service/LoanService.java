package com.example.demo.service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.LoanDTO;
import com.example.demo.model.Loan;
import com.example.demo.repository.LoanRepository;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    public Loan create(Loan loan) {
        return loanRepository.save(loan);
    }

    public List<Loan> getAll() {
        return loanRepository.findAll();
    }

    public Loan getById(UUID id) {
        return loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found"));
    }

    public Loan update(UUID id, Loan updated) {

        return loanRepository.findById(id).map(loan -> {
            loan.setAmount(updated.getAmount());
            loan.setInterestRate(updated.getInterestRate());
            loan.setStatus(updated.getStatus());
            loan.setUser(updated.getUser());
            loan.setAccount(updated.getAccount());
            return loanRepository.save(loan);
        }).orElseThrow(() -> new RuntimeException("Loan not found"));
    }

    public void delete(UUID id) {
        loanRepository.deleteById(id);
    }

    public LoanDTO convertToDTO(Loan loan) {
        LoanDTO dto = new LoanDTO();
        dto.setLoanId(loan.getLoan_id());
        dto.setAmount(loan.getAmount());
        dto.setInterestRate(loan.getInterestRate());
        dto.setStartDate(loan.getStartDate());
        dto.setEndDate(loan.getEndDate());
        dto.setIsRepaid(loan.isRepaid());
        dto.setLoanStatus(loan.getStatus() != null ? loan.getStatus().name() : null);

        if (loan.getUser() != null) {
            dto.setUserId(loan.getUser().getUser_id());
            dto.setUserFirstName(loan.getUser().getFirstName());
            dto.setUserLastName(loan.getUser().getLastName());
        }

        if (loan.getAccount() != null) {
            dto.setAccountId(loan.getAccount().getAccount_id());
            dto.setAccountNumber(loan.getAccount().getAccountNumber());
        }

        return dto;
    }

    public List<LoanDTO> convertToDTOList(List<Loan> loans) {
        return loans.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}
