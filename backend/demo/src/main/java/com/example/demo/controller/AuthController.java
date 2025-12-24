package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.example.demo.dto.*;
import com.example.demo.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/login/request")
    public ResponseEntity<?> requestLogin(@RequestBody LoginRequestDTO dto) {
        try {
            String message = authService.requestLogin(dto.getEmail(), dto.getPassword());
            return ResponseEntity.ok(new MessageResponse(message));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
    @PostMapping("/login/verify")
    public ResponseEntity<?> verifyLogin(@RequestBody VerifyOTPDTO dto) {
        try {
            LoginResponseDTO response = authService.verifyLoginOTP(dto.getEmail(), dto.getOtp());

            System.out.println("Sending response: " + response);
            System.out.println("Token: " + response.getToken());
            System.out.println("User: " + response.getUser());
            System.out.println("User Role: " + response.getUser().getUserRole());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Error in verifyLogin: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordDTO dto) {
        try {
            String message = authService.requestPasswordReset(dto.getEmail());
            return ResponseEntity.ok(new MessageResponse(message));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDTO dto) {
        try {
            String message = authService.resetPassword(dto.getToken(), dto.getNewPassword());
            return ResponseEntity.ok(new MessageResponse(message));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    static class MessageResponse {
        private String message;

        public MessageResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}