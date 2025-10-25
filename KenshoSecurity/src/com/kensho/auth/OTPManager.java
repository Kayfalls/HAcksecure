package com.kensho.auth;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class OTPManager {
    private static final int OTP_LENGTH = 6;
    private static final int OTP_EXPIRY_MINUTES = 5;
    
    private Map<String, OTPData> otpStore;
    
    public OTPManager() {
        this.otpStore = new HashMap<>();
    }
    
    public String generateOTP(String email) {
        String otp = generateRandomOTP();
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES);
        
        otpStore.put(email, new OTPData(otp, expiryTime));
        return otp;
    }
    
    public boolean verifyOTP(String email, String code) {
        OTPData otpData = otpStore.get(email);
        if (otpData == null) {
            return false;
        }
        
        if (LocalDateTime.now().isAfter(otpData.getExpiryTime())) {
            otpStore.remove(email); // Clean up expired OTP
            return false;
        }
        
        boolean isValid = otpData.getOtp().equals(code);
        if (isValid) {
            otpStore.remove(email); // Clean up used OTP
        }
        
        return isValid;
    }
    
    private String generateRandomOTP() {
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }
    
    private static class OTPData {
        private final String otp;
        private final LocalDateTime expiryTime;
        
        public OTPData(String otp, LocalDateTime expiryTime) {
            this.otp = otp;
            this.expiryTime = expiryTime;
        }
        
        public String getOtp() { return otp; }
        public LocalDateTime getExpiryTime() { return expiryTime; }
    }
}