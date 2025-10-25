package com.kensho.auth;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PasswordValidator {
    private static final int MIN_LENGTH = 8;
    private static final String[] WEAK_PASSWORDS = {"password", "12345678", "qwerty", "admin", "welcome"};
    
    public static class ValidationResult {
        public final boolean isValid;
        public final List<String> errors;
        public final List<String> suggestions;
        
        public ValidationResult(boolean isValid, List<String> errors, List<String> suggestions) {
            this.isValid = isValid;
            this.errors = errors;
            this.suggestions = suggestions;
        }
    }
    
    public static ValidationResult validatePassword(String password) {
        List<String> errors = new ArrayList<>();
        List<String> suggestions = new ArrayList<>();
        
        if (password.length() < MIN_LENGTH) {
            errors.add("Must be at least " + MIN_LENGTH + " characters");
            suggestions.add("Add " + (MIN_LENGTH - password.length()) + " more characters");
        }
        
        if (!password.matches(".*[A-Z].*")) {
            errors.add("Must contain at least one UPPERCASE letter (A-Z)");
            suggestions.add("Add a capital letter like A, B, C...");
        }
        
        if (!password.matches(".*[a-z].*")) {
            errors.add("Must contain at least one lowercase letter (a-z)");
            suggestions.add("Add a small letter like a, b, c...");
        }
        
        if (!password.matches(".*[0-9].*")) {
            errors.add("Must contain at least one number (0-9)");
            suggestions.add("Add a number like 1, 2, 3...");
        }
        
        if (!password.matches(".*[!@#$%^&*].*")) {
            errors.add("Must contain at least one special character (!@#$%^&*)");
            suggestions.add("Add a symbol like !, @, #, $...");
        }
        
        for (String weak : WEAK_PASSWORDS) {
            if (password.toLowerCase().equals(weak)) {
                errors.add("This is a commonly used weak password");
                suggestions.add("Choose a more unique password");
                break;
            }
        }
        
        return new ValidationResult(errors.isEmpty(), errors, suggestions);
    }
    
    public static String generateStrongPassword() {
        String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowercase = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String special = "!@#$%^&*";
        
        Random random = new Random();
        StringBuilder password = new StringBuilder();
        
        // Ensure at least one of each required character type
        password.append(uppercase.charAt(random.nextInt(uppercase.length())));
        password.append(lowercase.charAt(random.nextInt(lowercase.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));
        password.append(special.charAt(random.nextInt(special.length())));
        
        // Fill remaining length
        String allChars = uppercase + lowercase + digits + special;
        int remainingLength = Math.max(4, MIN_LENGTH - 4);
        for (int i = 0; i < remainingLength; i++) {
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }
        
        // Shuffle the password
        char[] passwordArray = password.toString().toCharArray();
        for (int i = passwordArray.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = passwordArray[i];
            passwordArray[i] = passwordArray[j];
            passwordArray[j] = temp;
        }
        
        return new String(passwordArray);
    }
    
    public static String[] getPasswordRequirements() {
        return new String[] {
            "At least " + MIN_LENGTH + " characters long",
            "At least one UPPERCASE letter (A-Z)",
            "At least one lowercase letter (a-z)",
            "At least one number (0-9)",
            "At least one special character (!@#$%^&*)",
            "Not a common weak password"
        };
    }
}