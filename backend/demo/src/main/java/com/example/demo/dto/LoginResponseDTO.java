package com.example.demo.dto;

public class LoginResponseDTO {
    private String message;
    private String token;
    private BankUserDTO user;
    
    public LoginResponseDTO() {}
    
    public LoginResponseDTO(String message, String token, BankUserDTO user) {
        this.message = message;
        this.token = token;
        this.user = user;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public BankUserDTO getUser() {
        return user;
    }
    
    public void setUser(BankUserDTO user) {
        this.user = user;
    }
}