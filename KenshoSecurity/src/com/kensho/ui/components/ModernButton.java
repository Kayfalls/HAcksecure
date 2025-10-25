package com.kensho.ui.components;

import com.kensho.Main;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ModernButton extends JButton {
    private Color normalColor;
    private Color hoverColor;
    private Color pressedColor;
    
    public ModernButton(String text) {
        this(text, Main.DARK_BLUE, Main.LIGHT_BLUE, Main.GOLD);
    }
    
    public ModernButton(String text, Color normal, Color hover, Color pressed) {
        super(text);
        this.normalColor = normal;
        this.hoverColor = hover;
        this.pressedColor = pressed;
        
        initializeButton();
    }
    
    private void initializeButton() {
        setBackground(normalColor);
        setForeground(Color.WHITE);
        setFocusPainted(false);
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        setFont(new Font("Arial", Font.BOLD, 12));
        
        // Add hover effects
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverColor);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(normalColor);
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(pressedColor);
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                setBackground(hoverColor);
            }
        });
    }
    
    // Special button types
    public static ModernButton createPrimaryButton(String text) {
        return new ModernButton(text, Main.DARK_BLUE, Main.LIGHT_BLUE, Main.GOLD);
    }
    
    public static ModernButton createSuccessButton(String text) {
        return new ModernButton(text, new Color(0, 150, 0), new Color(0, 200, 0), Main.GOLD);
    }
    
    public static ModernButton createGoldButton(String text) {
        return new ModernButton(text, Main.GOLD, new Color(255, 225, 0), new Color(255, 195, 0));
    }
}