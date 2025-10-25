package com.kensho.ui;

import com.kensho.Main;
import com.kensho.auth.AuthManager;
import com.kensho.auth.PasswordValidator;
import com.kensho.ui.components.ModernButton;
import com.kensho.ui.components.ModernTextField;
import com.kensho.ui.components.PasswordStrengthPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class RegisterFrame extends JFrame {
    private AuthManager authManager;
    private ModernTextField usernameField, emailField;
    private JPasswordField passwordField, confirmField;
    private PasswordStrengthPanel strengthPanel;
    private JLabel matchLabel;
    
    public RegisterFrame() {
        this.authManager = new AuthManager();
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Kensho Security - Register");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(550, 700);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Main.BLACK);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Create Your Account", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Main.GOLD);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Form panel
        JPanel formPanel = createFormPanel();
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Main.DARK_GRAY);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Main.DARK_BLUE, 2),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        
        // Form title
        JLabel formTitle = new JLabel("Account Information", SwingConstants.CENTER);
        formTitle.setFont(new Font("Arial", Font.BOLD, 14));
        formTitle.setForeground(Main.LIGHT_GRAY);
        formTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        panel.add(formTitle, BorderLayout.NORTH);
        
        // Form content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Main.DARK_GRAY);
        
        // Username and Email
        JPanel userPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        userPanel.setBackground(Main.DARK_GRAY);
        
        userPanel.add(createLabel("Username*"));
        userPanel.add(createLabel("Email Address*"));
        
        usernameField = new ModernTextField();
        emailField = new ModernTextField();
        userPanel.add(usernameField);
        userPanel.add(emailField);
        
        contentPanel.add(userPanel);
        contentPanel.add(Box.createVerticalStrut(15));
        
        // Password requirements
        JLabel reqTitle = new JLabel("Password Requirements:");
        reqTitle.setFont(new Font("Arial", Font.BOLD, 12));
        reqTitle.setForeground(Main.GOLD);
        contentPanel.add(reqTitle);
        
        for (String req : PasswordValidator.getPasswordRequirements()) {
            JLabel reqLabel = new JLabel("• " + req);
            reqLabel.setFont(new Font("Arial", Font.PLAIN, 10));
            reqLabel.setForeground(Main.LIGHT_GRAY);
            contentPanel.add(reqLabel);
        }
        
        contentPanel.add(Box.createVerticalStrut(15));
        
        // Password field
        contentPanel.add(createLabel("Password*"));
        passwordField = new JPasswordField();
        passwordField.setBackground(Main.DARK_GRAY);
        passwordField.setForeground(Color.WHITE);
        passwordField.setCaretColor(Main.GOLD);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Main.DARK_BLUE, 2),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        passwordField.addKeyListener(new PasswordKeyListener());
        contentPanel.add(passwordField);
        
        // Password strength panel
        strengthPanel = new PasswordStrengthPanel();
        contentPanel.add(strengthPanel);
        
        contentPanel.add(Box.createVerticalStrut(10));
        
        // Confirm password
        contentPanel.add(createLabel("Confirm Password*"));
        confirmField = new JPasswordField();
        confirmField.setBackground(Main.DARK_GRAY);
        confirmField.setForeground(Color.WHITE);
        confirmField.setCaretColor(Main.GOLD);
        confirmField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Main.DARK_BLUE, 2),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        confirmField.addKeyListener(new ConfirmKeyListener());
        contentPanel.add(confirmField);
        
        // Password match indicator
        matchLabel = new JLabel(" ");
        matchLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        contentPanel.add(matchLabel);
        
        contentPanel.add(Box.createVerticalStrut(15));
        
        // Generate password button
        ModernButton generateButton = ModernButton.createPrimaryButton("Generate Strong Password");
        generateButton.addActionListener(e -> generatePassword());
        contentPanel.add(generateButton);
        
        contentPanel.add(Box.createVerticalStrut(20));
        
        // Register button
        ModernButton registerButton = ModernButton.createGoldButton("Create Account");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.addActionListener(new RegisterAction());
        contentPanel.add(registerButton);
        
        panel.add(contentPanel, BorderLayout.CENTER);
        return panel;
    }
    
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 11));
        label.setForeground(Main.LIGHT_GRAY);
        return label;
    }
    
    private void generatePassword() {
        String password = PasswordValidator.generateStrongPassword();
        passwordField.setText(password);
        confirmField.setText(password);
        updatePasswordStrength();
        checkPasswordMatch();
    }
    
    private void updatePasswordStrength() {
        String password = new String(passwordField.getPassword());
        strengthPanel.updatePasswordStrength(password);
    }
    
    private void checkPasswordMatch() {
        String password = new String(passwordField.getPassword());
        String confirm = new String(confirmField.getPassword());
        
        if (confirm.isEmpty()) {
            matchLabel.setText(" ");
            matchLabel.setForeground(Main.LIGHT_GRAY);
        } else if (password.equals(confirm)) {
            matchLabel.setText("✓ Passwords match");
            matchLabel.setForeground(Main.GOLD);
        } else {
            matchLabel.setText("✗ Passwords do not match");
            matchLabel.setForeground(Color.RED);
        }
    }
    
    private class PasswordKeyListener extends KeyAdapter {
        @Override
        public void keyReleased(KeyEvent e) {
            updatePasswordStrength();
            checkPasswordMatch();
        }
    }
    
    private class ConfirmKeyListener extends KeyAdapter {
        @Override
        public void keyReleased(KeyEvent e) {
            checkPasswordMatch();
        }
    }
    
    private class RegisterAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirm = new String(confirmField.getPassword());
            
            // Validation
            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
                JOptionPane.showMessageDialog(RegisterFrame.this,
                    "Please fill in all required fields marked with *",
                    "Missing Information", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (!password.equals(confirm)) {
                JOptionPane.showMessageDialog(RegisterFrame.this,
                    "Passwords do not match. Please ensure both passwords are identical.",
                    "Password Mismatch", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            PasswordValidator.ValidationResult result = PasswordValidator.validatePassword(password);
            if (!result.isValid) {
                StringBuilder errorMsg = new StringBuilder("Please fix these password issues:\n\n");
                for (String error : result.errors) {
                    errorMsg.append("• ").append(error).append("\n");
                }
                JOptionPane.showMessageDialog(RegisterFrame.this,
                    errorMsg.toString(), "Weak Password", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Show loading
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            
            // Register user
            boolean success = authManager.registerUser(username, password, email);
            
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            
            if (success) {
                // Show OTP verification
                new OTPVerificationFrame(email, authManager).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(RegisterFrame.this,
                    "Username already exists or registration failed",
                    "Registration Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}