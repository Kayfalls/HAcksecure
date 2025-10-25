package com.kensho.ui;

import com.kensho.Main;
import com.kensho.network.WiFiScanner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class DashboardFrame extends JFrame {
    private String username;
    private WiFiScanner wifiScanner;
    
    public DashboardFrame(String username) {
        this.username = username;
        this.wifiScanner = new WiFiScanner();
        initializeUI();
        scanWiFiNetworks(); // Auto-scan on login
    }
    
    private void initializeUI() {
        setTitle("Kensho Security - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Main.BLACK);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Main.BLACK);
        
        JLabel titleLabel = new JLabel("Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Main.GOLD);
        
        JButton scanButton = new JButton("Scan WiFi Networks");
        scanButton.setBackground(Main.DARK_BLUE);
        scanButton.setForeground(Color.WHITE);
        scanButton.addActionListener(e -> scanWiFiNetworks());
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(scanButton, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Welcome panel
        JPanel welcomePanel = new JPanel(new GridLayout(1, 3, 10, 10));
        welcomePanel.setBackground(Main.BLACK);
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        welcomePanel.add(createInfoCard("Welcome", username, Main.GOLD));
        welcomePanel.add(createInfoCard("WiFi Scanner", "Scan nearby networks", Main.LIGHT_BLUE));
        welcomePanel.add(createInfoCard("Security", "Encrypted storage", Main.GOLD));
        
        mainPanel.add(welcomePanel, BorderLayout.CENTER);
        
        // Results area
        JTextArea resultsArea = new JTextArea();
        resultsArea.setEditable(false);
        resultsArea.setBackground(Main.DARK_GRAY);
        resultsArea.setForeground(Color.WHITE);
        resultsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(resultsArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Main.DARK_BLUE), "WiFi Scan Results"));
        scrollPane.setPreferredSize(new Dimension(700, 300));
        
        mainPanel.add(scrollPane, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createInfoCard(String title, String content, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Main.DARK_GRAY);
        card.setBorder(BorderFactory.createLineBorder(color, 2));
        
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(color);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        
        JLabel contentLabel = new JLabel(content, SwingConstants.CENTER);
        contentLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        contentLabel.setForeground(Main.LIGHT_GRAY);
        contentLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(contentLabel, BorderLayout.CENTER);
        
        return card;
    }
    
    private void scanWiFiNetworks() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
        // Run scan in background thread
        SwingWorker<List<WiFiScanner.WiFiNetwork>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<WiFiScanner.WiFiNetwork> doInBackground() throws Exception {
                return wifiScanner.scanNetworks();
            }
            
            @Override
            protected void done() {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                try {
                    List<WiFiScanner.WiFiNetwork> networks = get();
                    displayScanResults(networks);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(DashboardFrame.this,
                        "Error scanning networks: " + e.getMessage(),
                        "Scan Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        
        worker.execute();
    }
    
    private void displayScanResults(List<WiFiScanner.WiFiNetwork> networks) {
        JTextArea resultsArea = findResultsArea();
        if (resultsArea == null) return;
        
        StringBuilder results = new StringBuilder();
        results.append("✅ Scan completed! Found ").append(networks.size()).append(" networks:\n\n");
        
        for (int i = 0; i < networks.size(); i++) {
            WiFiScanner.WiFiNetwork network = networks.get(i);
            results.append(String.format("%d. %s\n", i + 1, network.getSsid()));
            results.append(String.format("    BSSID: %s\n", network.getBssid()));
            results.append(String.format("    Signal: %d dBm\n", network.getSignalStrength()));
            results.append(String.format("    Frequency: %s\n", network.getFrequency()));
            results.append(String.format("    Encryption: %s\n\n", network.getEncryption()));
        }
        
        results.append("📡 Total networks found: ").append(networks.size());
        resultsArea.setText(results.toString());
        
        JOptionPane.showMessageDialog(this,
            "Found " + networks.size() + " WiFi networks",
            "Scan Complete", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private JTextArea findResultsArea() {
        for (Component comp : getContentPane().getComponents()) {
            if (comp instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) comp;
                return (JTextArea) scrollPane.getViewport().getView();
            }
        }
        return null;
    }
}