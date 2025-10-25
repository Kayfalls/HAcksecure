package com.kensho.ui;

import com.kensho.Main;
import com.kensho.auth.AuthManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OTPVerificationFrame extends JFrame {
    private String email;
    private AuthManager authManager;
    private JTextField otpField;
    
    public OTPVerificationFrame(String email, AuthManager authManager) {
        this.email = email;
        this.authManager = authManager;
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Kensho Security - Verify Email");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Main.BLACK);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Verify Your Email", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Main.GOLD);
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Main.BLACK);
        
        // Instructions
        JLabel instructionLabel = new JLabel("<html><center>We've sent a 6-digit OTP code to:<br>" + email + "</center></html>");
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        instructionLabel.setForeground(Main.LIGHT_GRAY);
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel demoLabel = new JLabel("🔢 DEMO: Check your console for the OTP code!");
        demoLabel.setFont(new Font("Arial", Font.BOLD, 12));
        demoLabel.setForeground(Main.GOLD);
        demoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // OTP input
        JLabel otpLabel = new JLabel("Enter 6-digit OTP code:");
        otpLabel.setFont(new Font("Arial", Font.BOLD, 12));
        otpLabel.setForeground(Main.LIGHT_GRAY);
        otpLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        otpField = new JTextField(6);
        otpField.setFont(new Font("Arial", Font.BOLD, 16));
        otpField.setHorizontalAlignment(JTextField.CENTER);
        otpField.setMaximumSize(new Dimension(120, 30));
        
        // Buttons
        JButton verifyButton = new JButton("Verify OTP Code");
        verifyButton.setBackground(Main.GOLD);
        verifyButton.setForeground(Main.BLACK);
        verifyButton.setFont(new Font("Arial", Font.BOLD, 12));
        verifyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        verifyButton.addActionListener(new VerifyAction());
        
        JButton resendButton = new JButton("Resend OTP Code");
        resendButton.setBackground(Main.DARK_BLUE);
        resendButton.setForeground(Color.WHITE);
        resendButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        resendButton.addActionListener(e -> resendOTP());
        
        // Add components
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(instructionLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(demoLabel);
        contentPanel.add(Box.createVerticalStrut(30));
        contentPanel.add(otpLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(otpField);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(verifyButton);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(resendButton);
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);
    }
    
    private class VerifyAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String code = otpField.getText().trim();
            
            if (code.length() != 6 || !code.matches("\\d+")) {
                JOptionPane.showMessageDialog(OTPVerificationFrame.this,
                    "Please enter a valid 6-digit OTP code",
                    "Invalid Code", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (authManager.verifyOTP(email, code)) {
                JOptionPane.showMessageDialog(OTPVerificationFrame.this,
                    "🎉 Email verified successfully! You can now log in.",
                    "Verification Successful", JOptionPane.INFORMATION_MESSAGE);
                
                JOptionPane.showMessageDialog(OTPVerificationFrame.this,
                    "✅ Account created and verified!\n\n" +
                    "You can now login with your credentials.\n" +
                    "After login, the app will scan for nearby WiFi networks.",
                    "Registration Complete", JOptionPane.INFORMATION_MESSAGE);
                
                dispose();
                new com.kensho.ui.LoginFrame().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(OTPVerificationFrame.this,
                    "❌ The OTP code is incorrect or has expired.\n\n" +
                    "Please check your email and try again.",
                    "Invalid OTP Code", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void resendOTP() {
        authManager.regenerateOTP(email);
        JOptionPane.showMessageDialog(this,
            "A new OTP code has been sent to your email.",
            "OTP Resent", JOptionPane.INFORMATION_MESSAGE);
    }
}