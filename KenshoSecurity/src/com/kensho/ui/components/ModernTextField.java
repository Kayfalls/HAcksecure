package com.kensho.ui.components;

import com.kensho.Main;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class ModernTextField extends JTextField {
    private Border defaultBorder;
    private Border focusBorder;
    
    public ModernTextField() {
        this(20); // Default columns
    }
    
    public ModernTextField(int columns) {
        super(columns);
        initializeTextField();
    }
    
    public ModernTextField(String text) {
        super(text);
        initializeTextField();
    }
    
    private void initializeTextField() {
        setBackground(Main.DARK_GRAY);
        setForeground(Color.WHITE);
        setCaretColor(Main.GOLD);
        setSelectionColor(Main.LIGHT_BLUE);
        setSelectedTextColor(Color.WHITE);
        setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Create borders
        defaultBorder = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Main.DARK_BLUE, 2),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        );
        
        focusBorder = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Main.GOLD, 2),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        );
        
        setBorder(defaultBorder);
        
        // Add focus listener for border change
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                setBorder(focusBorder);
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                setBorder(defaultBorder);
            }
        });
    }
    
    public void setErrorState(boolean hasError) {
        if (hasError) {
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.RED, 2),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
            ));
        } else {
            setBorder(defaultBorder);
        }
    }
}