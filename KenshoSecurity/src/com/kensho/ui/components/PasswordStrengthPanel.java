package com.kensho.ui.components;

import com.kensho.Main;
import com.kensho.auth.PasswordValidator;
import javax.swing.*;
import java.awt.*;

public class PasswordStrengthPanel extends JPanel {
    private JProgressBar strengthBar;
    private JLabel strengthLabel;
    private JTextArea feedbackArea;
    
    public PasswordStrengthPanel() {
        initializePanel();
    }
    
    private void initializePanel() {
        setLayout(new BorderLayout());
        setBackground(Main.DARK_GRAY);
        
        // Strength bar
        JPanel barPanel = new JPanel(new BorderLayout());
        barPanel.setBackground(Main.DARK_GRAY);
        
        strengthLabel = new JLabel("Enter a password");
        strengthLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        strengthLabel.setForeground(Main.LIGHT_GRAY);
        
        strengthBar = new JProgressBar(0, 100);
        strengthBar.setStringPainted(false);
        strengthBar.setBackground(Main.DARK_GRAY);
        strengthBar.setForeground(Color.RED);
        
        barPanel.add(strengthLabel, BorderLayout.NORTH);
        barPanel.add(strengthBar, BorderLayout.CENTER);
        
        // Feedback area
        feedbackArea = new JTextArea(4, 20);
        feedbackArea.setEditable(false);
        feedbackArea.setBackground(Main.DARK_GRAY);
        feedbackArea.setForeground(Main.LIGHT_GRAY);
        feedbackArea.setFont(new Font("Arial", Font.PLAIN, 9));
        JScrollPane feedbackScroll = new JScrollPane(feedbackArea);
        feedbackScroll.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        
        add(barPanel, BorderLayout.NORTH);
        add(feedbackScroll, BorderLayout.CENTER);
    }
    
    public void updatePasswordStrength(String password) {
        PasswordValidator.ValidationResult result = PasswordValidator.validatePassword(password);
        
        if (password.isEmpty()) {
            strengthLabel.setText("Enter a password");
            strengthLabel.setForeground(Main.LIGHT_GRAY);
            strengthBar.setValue(0);
            strengthBar.setForeground(Color.RED);
            feedbackArea.setText("");
            return;
        }
        
        // Calculate strength percentage
        int requirementsMet = 0;
        int totalRequirements = 5; // length, upper, lower, number, special
        
        if (password.length() >= 8) requirementsMet++;
        if (password.matches(".*[A-Z].*")) requirementsMet++;
        if (password.matches(".*[a-z].*")) requirementsMet++;
        if (password.matches(".*[0-9].*")) requirementsMet++;
        if (password.matches(".*[!@#$%^&*].*")) requirementsMet++;
        
        int strength = (requirementsMet * 100) / totalRequirements;
        strengthBar.setValue(strength);
        
        // Update UI based on strength
        if (result.isValid) {
            strengthLabel.setText("✓ Strong password - Meets all requirements");
            strengthLabel.setForeground(Main.GOLD);
            strengthBar.setForeground(Main.GOLD);
            feedbackArea.setText("");
        } else {
            strengthLabel.setText("✗ Password needs improvement");
            strengthLabel.setForeground(Color.RED);
            
            // Set bar color based on strength
            if (strength >= 80) {
                strengthBar.setForeground(Main.GOLD);
            } else if (strength >= 60) {
                strengthBar.setForeground(Color.ORANGE);
            } else {
                strengthBar.setForeground(Color.RED);
            }
            
            // Build feedback text
            StringBuilder feedback = new StringBuilder();
            for (int i = 0; i < result.errors.size(); i++) {
                feedback.append("✗ ").append(result.errors.get(i)).append("\n");
                feedback.append("  → ").append(result.suggestions.get(i)).append("\n\n");
            }
            feedbackArea.setText(feedback.toString());
        }
    }
    
    public void clear() {
        strengthLabel.setText("Enter a password");
        strengthLabel.setForeground(Main.LIGHT_GRAY);
        strengthBar.setValue(0);
        strengthBar.setForeground(Color.RED);
        feedbackArea.setText("");
    }
}