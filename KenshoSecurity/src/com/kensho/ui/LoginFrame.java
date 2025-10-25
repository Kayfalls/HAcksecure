package com.kensho.ui;

import com.kensho.Main;
import com.kensho.auth.AuthManager;
import com.kensho.ui.components.ModernButton;
import com.kensho.ui.components.ModernTextField;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private AuthManager authManager;
    private ModernTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox showPasswordCheck;
    
    public LoginFrame() {
        this.authManager = new AuthManager();
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Kensho Security - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main container
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Main.BLACK);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        // Header
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Login form
        JPanel formPanel = createFormPanel();
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Main.BLACK);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));
        
        JLabel titleLabel = new JLabel("Kensho Security", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Main.GOLD);
        
        JLabel subtitleLabel = new JLabel("AI-Powered Network Security", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitleLabel.setForeground(Main.LIGHT_GRAY);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(subtitleLabel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Main.DARK_GRAY);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Main.DARK_BLUE, 2),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        
        // Form title
        JLabel formTitle = new JLabel("Sign In to Your Account", SwingConstants.CENTER);
        formTitle.setFont(new Font("Arial", Font.BOLD, 16));
        formTitle.setForeground(Main.LIGHT_GRAY);
        formTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        panel.add(formTitle, BorderLayout.NORTH);
        
        // Form fields
        JPanel fieldsPanel = new JPanel(new GridLayout(0, 1, 0, 15));
        fieldsPanel.setBackground(Main.DARK_GRAY);
        
        // Username field
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        usernameLabel.setForeground(Main.LIGHT_GRAY);
        
        usernameField = new ModernTextField();
        
        // Password field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 12));
        passwordLabel.setForeground(Main.LIGHT_GRAY);
        
        passwordField = new JPasswordField();
        passwordField.setBackground(Main.DARK_GRAY);
        passwordField.setForeground(Color.WHITE);
        passwordField.setCaretColor(Main.GOLD);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Main.DARK_BLUE, 2),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        
        // Show password checkbox
        showPasswordCheck = new JCheckBox("Show password");
        showPasswordCheck.setBackground(Main.DARK_GRAY);
        showPasswordCheck.setForeground(Main.LIGHT_GRAY);
        showPasswordCheck.addActionListener(e -> {
            if (showPasswordCheck.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('•');
            }
        });
        
        // Login button
        ModernButton loginButton = ModernButton.createPrimaryButton("Sign In");
        loginButton.addActionListener(new LoginAction());
        
        // Register link
        ModernButton registerButton = new ModernButton("Create account", 
            Main.BLACK, Main.BLACK, Main.BLACK);
        registerButton.setForeground(Main.GOLD);
        registerButton.addActionListener(e -> {
            new RegisterFrame().setVisible(true);
            dispose();
        });
        
        // Add components to fields panel
        fieldsPanel.add(usernameLabel);
        fieldsPanel.add(usernameField);
        fieldsPanel.add(passwordLabel);
        fieldsPanel.add(passwordField);
        fieldsPanel.add(showPasswordCheck);
        fieldsPanel.add(loginButton);
        
        // Links panel
        JPanel linksPanel = new JPanel(new BorderLayout());
        linksPanel.setBackground(Main.DARK_GRAY);
        
        ModernButton forgotButton = new ModernButton("Forgot password?", 
            Main.DARK_GRAY, Main.DARK_GRAY, Main.DARK_GRAY);
        forgotButton.setForeground(Main.LIGHT_BLUE);
        
        linksPanel.add(forgotButton, BorderLayout.WEST);
        linksPanel.add(registerButton, BorderLayout.EAST);
        
        panel.add(fieldsPanel, BorderLayout.CENTER);
        panel.add(linksPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private class LoginAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(LoginFrame.this, 
                    "Please enter both username and password", 
                    "Input Required", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Show loading
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            
            // Authenticate user
            boolean success = authManager.authenticateUser(username, password);
            
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            
            if (success) {
                JOptionPane.showMessageDialog(LoginFrame.this, 
                    "Welcome back, " + username + "!", 
                    "Login Successful", JOptionPane.INFORMATION_MESSAGE);
                
                // Open dashboard and scan WiFi
                new DashboardFrame(username).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(LoginFrame.this, 
                    "Invalid username or password", 
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
            }
        }
    }
}