package com.example.demo.controller;

import com.example.demo.dto.AccountDTO;
import com.example.demo.dto.AccountResponseDTO;
import com.example.demo.dto.LoanDTO;
import com.example.demo.dto.TransactionDTO;
import com.example.demo.service.AccountService;
import com.example.demo.model.Account;
import com.example.demo.model.BankUser;
import com.example.demo.model.Loan;
import com.example.demo.model.Transaction;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.BankUserRepository;
import com.example.demo.repository.LoanRepository;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.service.AccountService;
import com.example.demo.service.LoanService;
import com.example.demo.service.TransactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/client")
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = { "Authorization", "Content-Type",
        "Accept" }, allowCredentials = "true")
public class ClientController {

    @Autowired
    private BankUserRepository bankUserRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private TransactionRepository transactionRepository;


    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private LoanService loanService;

    private BankUser getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return bankUserRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @GetMapping("/accounts")
    public ResponseEntity<?> getMyAccounts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String search) {
        try {
            BankUser user = getAuthenticatedUser();
            Pageable pageable = PageRequest.of(page, size, Sort.by("accountNumber").ascending());

            Page<Account> accountsPage;
            if (search != null && !search.trim().isEmpty()) {
                accountsPage = accountRepository.searchAccountsByUser(user, search, pageable);
            } else {
                accountsPage = accountRepository.findByUser(user, pageable);
            }

            Page<AccountResponseDTO> dtoPage = accountsPage.map(accountService::convertToDTO);

            return ResponseEntity.ok(dtoPage);
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving accounts: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/transactions")
    public ResponseEntity<?> getMyTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String search) {
        try {
            BankUser user = getAuthenticatedUser();
            Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());

            // Get all accounts for this user
            List<Account> userAccounts = accountRepository.findByUser(user);

            if (userAccounts.isEmpty()) {
                return ResponseEntity.ok(Page.empty());
            }

            Page<Transaction> transactionsPage;
            if (search != null && !search.trim().isEmpty()) {
                transactionsPage = transactionRepository.searchTransactionsByAccounts(userAccounts, search, pageable);
            } else {
                transactionsPage = transactionRepository.findByAccountIn(userAccounts, pageable);
            }

            Page<TransactionDTO> dtoPage = transactionsPage.map(transactionService::convertToDTO);
            return ResponseEntity.ok(dtoPage);
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving transactions: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/loans")
    public ResponseEntity<?> getMyLoans(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String search) {
        try {
            BankUser user = getAuthenticatedUser();
            Pageable pageable = PageRequest.of(page, size, Sort.by("startDate").descending());

            Page<Loan> loansPage;
            if (search != null && !search.trim().isEmpty()) {
                loansPage = loanRepository.searchLoansByUser(user, search, pageable);
            } else {
                loansPage = loanRepository.findByUser(user, pageable);
            }

            Page<LoanDTO> dtoPage = loansPage.map(loanService::convertToDTO);
            return ResponseEntity.ok(dtoPage);
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving loans: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
        try {
            BankUser user = getAuthenticatedUser();

            Map<String, Object> profile = new HashMap<>();
            profile.put("userId", user.getUser_id());
            profile.put("firstName", user.getFirstName());
            profile.put("lastName", user.getLastName());
            profile.put("email", user.getEmail());
            profile.put("phoneNumber", user.getPhoneNumber());
            profile.put("role", user.getUser_Role().name());

            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving profile: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create-account")
    public ResponseEntity<?> createAccount(@RequestBody AccountDTO dto) {
        try {
            BankUser user = getAuthenticatedUser();

            // Generate account number if not provided
            String accountNumber = dto.getAccountNumber();
            if (accountNumber == null || accountNumber.trim().isEmpty()) {
                accountNumber = "ACC" + System.currentTimeMillis();
            }

            // Check if account number already exists
            if (accountRepository.findByAccountNumber(accountNumber).isPresent()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Account number already exists"));
            }

            Account account = new Account();
            account.setAccountNumber(accountNumber);
            account.setBalance(dto.getBalance());
            account.setAccount_type(com.example.demo.model.EAccount.valueOf(dto.getAccountType()));
            account.setUser(user);
            account.setActive(true);
            account.setVerified(false); // Requires admin verification

            Account saved = accountService.createAccount(account);
            AccountResponseDTO responseDTO = accountService.convertToDTO(saved);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Account created successfully",
                    "data", responseDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Error creating account: " + e.getMessage()));
        }
    }

    @PostMapping("/create-transaction")
    public ResponseEntity<?> createTransaction(@RequestBody Map<String, Object> request) {
        try {
            BankUser user = getAuthenticatedUser();
            String accountNumber = (String) request.get("accountNumber");
            Double amount = Double.parseDouble(request.get("amount").toString());

            if (accountNumber == null || amount == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Account number and amount are required"));
            }

            Account account = accountRepository.findByAccountNumber(accountNumber)
                    .orElseThrow(() -> new RuntimeException("Account not found"));

            // Verify account belongs to user
            if (!account.getUser().getUser_id().equals(user.getUser_id())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("message", "You can only make transactions on your own accounts"));
            }

            // Check if account is active
            if (!account.isActive()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Account is not active"));
            }

            // Create transaction
            Transaction transaction = new Transaction();
            transaction.setAccount(account);
            transaction.setAmount(amount);
            transaction.setTimestamp(java.time.LocalDateTime.now());

            Transaction savedTransaction = transactionService.createTransaction(transaction);

            // Update account balance
            double newBalance = account.getBalance() + amount;
            if (newBalance < 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Insufficient balance"));
            }
            account.setBalance(newBalance);
            accountRepository.save(account);

            TransactionDTO transactionDTO = transactionService.convertToDTO(savedTransaction);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Transaction completed successfully",
                    "data", transactionDTO,
                    "newBalance", newBalance));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Error creating transaction: " + e.getMessage()));
        }
    }

    @PostMapping("/apply-loan")
    public ResponseEntity<?> applyForLoan(@RequestBody Map<String, Object> request) {
        try {
            BankUser user = getAuthenticatedUser();
            Double amount = Double.parseDouble(request.get("amount").toString());
            Double interestRate = Double.parseDouble(request.get("interestRate").toString());
            String accountNumber = (String) request.get("accountNumber");
            Integer months = Integer.parseInt(request.get("months").toString());

            if (amount == null || interestRate == null || accountNumber == null || months == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "All fields are required"));
            }

            Account account = accountRepository.findByAccountNumber(accountNumber)
                    .orElseThrow(() -> new RuntimeException("Account not found"));

            // Verify account belongs to user
            if (!account.getUser().getUser_id().equals(user.getUser_id())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("message", "You can only apply for loans using your own accounts"));
            }

            // Create loan
            Loan loan = new Loan();
            loan.setAmount(amount);
            loan.setInterestRate(interestRate);
            loan.setUser(user);
            loan.setAccount(account);
            loan.setStatus(com.example.demo.model.ELoan.PENDING);
            loan.setRepaid(false);
            loan.setStartDate(java.time.LocalDateTime.now());
            loan.setEndDate(java.time.LocalDateTime.now().plusMonths(months));

            Loan savedLoan = loanService.create(loan);
            LoanDTO loanDTO = loanService.convertToDTO(savedLoan);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Loan application submitted successfully",
                    "data", loanDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Error applying for loan: " + e.getMessage()));
        }
    }
}
