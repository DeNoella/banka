package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OTPService {

    @Autowired
    private EmailService emailService;

    private Map<String, String> otpStore = new HashMap<>();
    private Map<String, Long> otpTimestamp = new HashMap<>();
    private static final long OTP_VALIDITY = 5 * 60 * 1000; // 5 minutes

    public String generateOTP(String email, String userName) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        otpStore.put(email, otp);
        otpTimestamp.put(email, System.currentTimeMillis());

        // Send OTP via email
        emailService.sendOTPEmail(email, otp, userName);

        // Also log it in console for testing/debugging
        System.out.println("🔐OTP Generated for " + email + ": " + otp);

        return otp;
    }

    public boolean verifyOTP(String email, String otp) {
        if (!otpStore.containsKey(email)) {
            System.out.println(" No OTP found for email: " + email);
            return false;
        }

        long timestamp = otpTimestamp.get(email);
        if (System.currentTimeMillis() - timestamp > OTP_VALIDITY) {
            System.out.println("OTP expired for email: " + email);
            otpStore.remove(email);
            otpTimestamp.remove(email);
            return false;
        }

        boolean isValid = otpStore.get(email).equals(otp);
        if (isValid) {
            System.out.println("OTP verified successfully for: " + email);
            otpStore.remove(email);
            otpTimestamp.remove(email);
        } else {
            System.out.println(" Invalid OTP for email: " + email);
        }

        return isValid;
    }

    public void clearOTP(String email) {
        otpStore.remove(email);
        otpTimestamp.remove(email);
    }
}