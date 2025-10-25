package com.kensho.auth;

import com.kensho.data.DatabaseManager;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class AuthManager {
    private DatabaseManager dbManager;
    private OTPManager otpManager;
    
    public AuthManager() {
        this.dbManager = new DatabaseManager();
        this.otpManager = new OTPManager();
    }
    
    public boolean registerUser(String username, String password, String email) {
        // Step 1: Validate input
        if (username == null || username.trim().isEmpty() || 
            password == null || password.isEmpty() || 
            email == null || email.trim().isEmpty()) {
            return false;
        }
        
        // Step 2: Check if user already exists
        if (dbManager.userExists(username)) {
            return false;
        }
        
        // Step 3: Hash password and create user
        String salt = generateSalt();
        String passwordHash = hashPassword(password, salt);
        
        boolean userCreated = dbManager.createUser(username, passwordHash, salt, email);
        
        if (userCreated) {
            // Step 4: Generate and display OTP (for demo)
            String otp = otpManager.generateOTP(email);
            System.out.println("🔐 [SECURITY OTP] Verification code sent to: " + email);
            System.out.println("🔢 [YOUR OTP CODE]: " + otp);
            System.out.println("⏰ [NOTE] This code expires in 5 minutes");
            return true;
        }
        
        return false;
    }
    
    public boolean verifyOTP(String email, String code) {
        return otpManager.verifyOTP(email, code);
    }

    public String regenerateOTP(String email) {
        String otp = otpManager.generateOTP(email);
        System.out.println("🔐 [SECURITY OTP] Verification code sent to: " + email);
        System.out.println("🔢 [YOUR OTP CODE]: " + otp);
        System.out.println("⏰ [NOTE] This code expires in 5 minutes");
        return otp;
    }
    
    public boolean authenticateUser(String username, String password) {
        DatabaseManager.User user = dbManager.getUser(username);
        if (user == null) {
            return false;
        }
        
        String testHash = hashPassword(password, user.getSalt());
        return testHash.equals(user.getPasswordHash());
    }
    
    private String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] hashedPassword = md.digest(password.getBytes());
            
            // Additional iterations for security
            for (int i = 0; i < 100000; i++) {
                hashedPassword = md.digest(hashedPassword);
            }
            
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (Exception e) {
            throw new RuntimeException("Password hashing failed", e);
        }
    }
    
    private String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
}