package com.kensho;

import com.kensho.ui.LoginFrame;
import javax.swing.*;
import java.awt.*;

public class Main {
    // Kensho Color Scheme
    public static final Color GOLD = new Color(255, 215, 0);
    public static final Color DARK_BLUE = new Color(0, 75, 150);
    public static final Color LIGHT_BLUE = new Color(100, 180, 255);
    public static final Color BLACK = new Color(20, 20, 20);
    public static final Color DARK_GRAY = new Color(40, 40, 40);
    public static final Color LIGHT_GRAY = new Color(200, 200, 200);
    
    public static void main(String[] args) {
        // Set modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
            setCustomTheme();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Start the application
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
    
    private static void setCustomTheme() {
        // Apply Kensho color theme
        UIManager.put("Panel.background", BLACK);
        UIManager.put("Button.background", DARK_BLUE);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.focus", GOLD);
        
        UIManager.put("TextField.background", DARK_GRAY);
        UIManager.put("TextField.foreground", Color.WHITE);
        UIManager.put("TextField.caretForeground", GOLD);
        UIManager.put("TextField.border", BorderFactory.createLineBorder(DARK_BLUE, 2));
        
        UIManager.put("PasswordField.background", DARK_GRAY);
        UIManager.put("PasswordField.foreground", Color.WHITE);
        UIManager.put("PasswordField.caretForeground", GOLD);
        UIManager.put("PasswordField.border", BorderFactory.createLineBorder(DARK_BLUE, 2));
        
        UIManager.put("Label.foreground", LIGHT_GRAY);
        UIManager.put("CheckBox.foreground", LIGHT_GRAY);
        UIManager.put("CheckBox.background", BLACK);
        
        UIManager.put("TextArea.background", DARK_GRAY);
        UIManager.put("TextArea.foreground", Color.WHITE);
        UIManager.put("TextArea.caretForeground", GOLD);
    }
}