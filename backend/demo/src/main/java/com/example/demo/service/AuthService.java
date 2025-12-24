package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.model.BankUser;
import com.example.demo.model.ERole;
import com.example.demo.model.PasswordResetToken;
import com.example.demo.repository.BankUserRepository;
import com.example.demo.repository.PasswordResetTokenRepository;
import com.example.demo.security.JwtUtils;

import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private BankUserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private com.example.demo.repository.LocationRepository locationRepository;

    @Autowired
    private OTPService otpService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String requestLogin(String email, String password) {
        System.out.println("👉 AuthService: Received login request for: " + email);
        BankUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        // if (!passwordEncoder.matches(password, user.getPassword())) {
        // throw new RuntimeException("Invalid password");
        // }

        String userName = user.getFirstName() + " " + user.getLastName();
        String otp = otpService.generateOTP(email, userName);

        return "OTP has been sent to your email: " + email + ". Please check your inbox and spam folder.";
    }

    // ... (verifyLoginOTP remains same)

    public LoginResponseDTO verifyLoginOTP(String email, String otp) {
        BankUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!otpService.verifyOTP(email, otp)) {
            throw new RuntimeException("Invalid or expired OTP. Please request a new one.");
        }

        String token = jwtUtils.generateToken(
                user.getEmail(),
                user.getUser_id(),
                user.getUser_Role().name());

        BankUserDTO userDTO = convertToDTO(user);

        return new LoginResponseDTO("Login successful", token, userDTO);
    }

    public BankUserDTO createManager(CreateManagerDTO dto) {
        // Get authenticated admin email from SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String adminEmail = authentication.getName(); // This gets the email from JWT token

        // Verify admin
        BankUser admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        if (admin.getUser_Role() != ERole.ADMIN) {
            throw new RuntimeException("Only admins can create managers");
        }

        // Check if email exists
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Generate temporary password
        String tempPassword = generateTempPassword();

        // Create manager
        BankUser manager = new BankUser();
        manager.setFirstName(dto.getFirstName());
        manager.setLastName(dto.getLastName());
        manager.setEmail(dto.getEmail());
        manager.setPhoneNumber(dto.getPhoneNumber());
        manager.setPassword(passwordEncoder.encode(tempPassword));
        manager.setUser_Role(ERole.MANAGER);

        BankUser savedManager = userRepository.save(manager);

        // Send welcome email with credentials
        String userName = savedManager.getFirstName() + " " + savedManager.getLastName();
        emailService.sendWelcomeEmail(
                savedManager.getEmail(),
                userName,
                "MANAGER",
                tempPassword);

        return convertToDTO(savedManager);
    }

    public BankUserDTO createUser(CreateUserDTO dto) {
        // Get authenticated manager email from SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String managerEmail = authentication.getName();

        // Verify manager
        BankUser manager = userRepository.findByEmail(managerEmail)
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        if (manager.getUser_Role() != ERole.MANAGER) {
            throw new RuntimeException("Only managers can create users");
        }

        // Verify role
        if (dto.getRole() != ERole.CASHIER && dto.getRole() != ERole.CLIENT) {
            throw new RuntimeException("Managers can only create CASHIER or CLIENT roles");
        }

        // Check if email exists
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Handle Location
        com.example.demo.model.Location location = null;
        if (dto.getRole() == ERole.CLIENT) {
            if (dto.getLocationCode() == null || dto.getLocationCode().isEmpty()) {
                throw new RuntimeException("Location is required for Clients");
            }
            location = locationRepository.findByCode(dto.getLocationCode())
                    .orElseThrow(() -> new RuntimeException("Location not found with code: " + dto.getLocationCode()));
        }

        // Generate temporary password
        String tempPassword = generateTempPassword();

        // Create user
        BankUser user = new BankUser();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(tempPassword));
        user.setUser_Role(dto.getRole());
        user.setLocation(location); // Set the location

        BankUser savedUser = userRepository.save(user);

        // Send welcome email with credentials
        String userName = savedUser.getFirstName() + " " + savedUser.getLastName();
        emailService.sendWelcomeEmail(
                savedUser.getEmail(),
                userName,
                dto.getRole().name(),
                tempPassword);

        return convertToDTO(savedUser);
    }

    private String generateTempPassword() {
        // Generate random 8-character password
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%";
        StringBuilder password = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }

    private BankUserDTO convertToDTO(BankUser user) {
        BankUserDTO dto = new BankUserDTO();
        dto.setUserId(user.getUser_id());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setUserRole(user.getUser_Role().name());
        return dto;
    }

    @Transactional
    public String requestPasswordReset(String email) {
        BankUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        // Delete any existing tokens for this email
        passwordResetTokenRepository.deleteByEmail(email);

        // Generate a unique token
        String token = UUID.randomUUID().toString();

        // Save the token
        PasswordResetToken resetToken = new PasswordResetToken(token, email);
        passwordResetTokenRepository.save(resetToken);

        // Send password reset email
        String userName = user.getFirstName() + " " + user.getLastName();
        emailService.sendPasswordResetEmail(email, userName, token);

        return "Password reset link has been sent to your email: " + email;
    }

    @Transactional
    public String resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid password reset token"));

        if (resetToken.isUsed()) {
            throw new RuntimeException("This password reset token has already been used");
        }

        if (resetToken.isExpired()) {
            throw new RuntimeException("This password reset token has expired. Please request a new one.");
        }

        // Find the user
        BankUser user = userRepository.findByEmail(resetToken.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update the password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Mark token as used
        resetToken.setUsed(true);
        passwordResetTokenRepository.save(resetToken);

        return "Password has been reset successfully. You can now login with your new password.";
    }
}