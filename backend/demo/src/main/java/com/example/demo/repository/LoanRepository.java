package com.example.demo.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.BankUser;
import com.example.demo.model.Loan;

import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<Loan, UUID> {

    List<Loan> findByUser(BankUser user);

    long countByStatus(com.example.demo.model.ELoan status);

    Page<Loan> findByUser(BankUser user, Pageable pageable);

    Page<Loan> findAll(Pageable pageable);

    @Query("SELECT l FROM Loan l WHERE " +
            "LOWER(l.user.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(l.user.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(l.user.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(str(l.status)) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "str(l.amount) LIKE CONCAT('%', :search, '%')")
    Page<Loan> searchLoans(@Param("search") String search, Pageable pageable);

    @Query("SELECT l FROM Loan l WHERE l.user = :user AND (" +
            "LOWER(str(l.status)) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "str(l.amount) LIKE CONCAT('%', :search, '%'))")
    Page<Loan> searchLoansByUser(@Param("user") BankUser user, @Param("search") String search, Pageable pageable);
}
