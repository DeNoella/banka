package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.name:BANKA}")
    private String appName;

    public void sendOTPEmail(String toEmail, String otp, String userName) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Your OTP for " + appName + " Login");

            String emailBody = String.format(
                    "Dear %s,\n\n" +
                            "Your One-Time Password (OTP) for login is: %s\n\n" +
                            "This OTP is valid for 5 minutes.\n\n" +
                            "If you did not request this OTP, please ignore this email.\n\n" +
                            "Best regards,\n" +
                            "%s Security Team",
                    userName, otp, appName);

            message.setText(emailBody);

            mailSender.send(message);

            System.out.println("OTP email sent successfully to: " + toEmail);

        } catch (Exception e) {
            System.err.println(" Failed to send email: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to send OTP email: " + e.getMessage());
        }
    }

    public void sendWelcomeEmail(String toEmail, String userName, String role, String tempPassword) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Welcome to " + appName + " - Account Created");

            String emailBody = String.format(
                    "Dear %s,\n\n" +
                            "Your account has been successfully created at %s.\n\n" +
                            "Account Details:\n" +
                            "Role: %s\n" +
                            "Email: %s\n" +
                            "Temporary Password: %s\n\n" +
                            "For security reasons, please:\n" +
                            "1. Login using OTP verification\n" +
                            "2. Change your password after first login\n\n" +
                            "To login:\n" +
                            "1. Go to the login page\n" +
                            "2. Enter your email: %s\n" +
                            "3. Request OTP\n" +
                            "4. Enter the OTP sent to this email\n\n" +
                            "Best regards,\n" +
                            "%s Team",
                    userName, appName, role, toEmail, tempPassword, toEmail, appName);

            message.setText(emailBody);

            mailSender.send(message);

            System.out.println(" Welcome email sent successfully to: " + toEmail);

        } catch (Exception e) {
            System.err.println(" Failed to send welcome email: " + e.getMessage());
            e.printStackTrace();
            // Don't throw exception for welcome email - it's not critical
        }
    }

    public void sendPasswordResetEmail(String toEmail, String userName, String resetToken) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject(appName + " - Password Reset Request");

            // In production, this should be the actual frontend URL
            String resetLink = "http://localhost:5173/reset-password?token=" + resetToken;

            String emailBody = String.format(
                    "Dear %s,\n\n" +
                            "We received a request to reset your password for your %s account.\n\n" +
                            "To reset your password, please click the link below:\n" +
                            "%s\n\n" +
                            "This link will expire in 1 hour.\n\n" +
                            "If you did not request a password reset, please ignore this email or contact support if you have concerns.\n\n"
                            +
                            "For security reasons, never share this link with anyone.\n\n" +
                            "Best regards,\n" +
                            "%s Security Team",
                    userName, appName, resetLink, appName);

            message.setText(emailBody);

            mailSender.send(message);

            System.out.println("Password reset email sent successfully to: " + toEmail);

        } catch (Exception e) {
            System.err.println(" Failed to send password reset email: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to send password reset email. Please try again.");
        }
    }
}