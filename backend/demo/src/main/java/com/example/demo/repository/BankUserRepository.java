package com.example.demo.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.BankUser;
import com.example.demo.model.Location;

@Repository
public interface BankUserRepository extends JpaRepository<BankUser, UUID> {
       Boolean existsByEmail(String email); // Save

       @Query("SELECT COUNT(u) FROM BankUser u WHERE u.user_Role = ?1")
       long countByUser_Role(com.example.demo.model.ERole role);

       Optional<BankUser> findByEmail(String email); // Update

       List<BankUser> findByFirstNameAndLastName(String firstName, String lastName);

       List<BankUser> findByLocation(Location location);

       List<BankUser> findByFirstNameStartingWith(String firstName);

       List<BankUser> findByLastNameEndingWith(String lastName);

       Page<BankUser> findAll(Pageable pageable);

       @Query("SELECT u FROM BankUser u WHERE " +
                     "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
                     "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
                     "LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
                     "LOWER(u.phoneNumber) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
                     "LOWER(CAST(u.user_Role AS string)) LIKE LOWER(CONCAT('%', :search, '%'))")
       Page<BankUser> searchUsers(@Param("search") String search, Pageable pageable);
}
