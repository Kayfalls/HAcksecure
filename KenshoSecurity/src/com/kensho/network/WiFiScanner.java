package com.kensho.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class WiFiScanner {
    
    public static class WiFiNetwork {
        private String ssid;
        private String bssid;
        private int signalStrength;
        private String frequency;
        private String encryption;
        
        public WiFiNetwork(String ssid, String bssid, int signalStrength, String frequency, String encryption) {
            this.ssid = ssid;
            this.bssid = bssid;
            this.signalStrength = signalStrength;
            this.frequency = frequency;
            this.encryption = encryption;
        }
        
        // Getters
        public String getSsid() { return ssid; }
        public String getBssid() { return bssid; }
        public int getSignalStrength() { return signalStrength; }
        public String getFrequency() { return frequency; }
        public String getEncryption() { return encryption; }
    }
    
    public List<WiFiNetwork> scanNetworks() {
        List<WiFiNetwork> networks = new ArrayList<>();
        String os = System.getProperty("os.name").toLowerCase();
        
        try {
            if (os.contains("win")) {
                networks = scanWindowsNetworks();
            } else {
                // For demo purposes - return simulated networks
                networks = getDemoNetworks();
            }
        } catch (Exception e) {
            System.err.println("Error scanning networks: " + e.getMessage());
            networks = getDemoNetworks();
        }
        
        return networks;
    }
    
    private List<WiFiNetwork> scanWindowsNetworks() throws Exception {
        List<WiFiNetwork> networks = new ArrayList<>();
        
        Process process = Runtime.getRuntime().exec("netsh wlan show networks mode=bssid");
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        
        String line;
        String currentSsid = null;
        String currentBssid = null;
        int currentSignal = 0;
        String currentEncryption = "Unknown";
        
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            
            if (line.startsWith("SSID ")) {
                if (currentSsid != null && currentBssid != null) {
                    networks.add(new WiFiNetwork(currentSsid, currentBssid, currentSignal, "2.4 GHz", currentEncryption));
                }
                currentSsid = line.substring(5).trim();
            } else if (line.startsWith("BSSID ")) {
                currentBssid = line.substring(6).trim();
            } else if (line.startsWith("Signal")) {
                String[] parts = line.split(":");
                if (parts.length > 1) {
                    try {
                        currentSignal = Integer.parseInt(parts[1].trim().replace("%", ""));
                    } catch (NumberFormatException e) {
                        currentSignal = 0;
                    }
                }
            } else if (line.contains("Authentication") && line.contains("WPA2")) {
                currentEncryption = "WPA2";
            } else if (line.contains("Authentication") && line.contains("WPA3")) {
                currentEncryption = "WPA3";
            } else if (line.contains("Authentication") && line.contains("Open")) {
                currentEncryption = "Open";
            }
        }
        
        // Add the last network
        if (currentSsid != null && currentBssid != null) {
            networks.add(new WiFiNetwork(currentSsid, currentBssid, currentSignal, "2.4 GHz", currentEncryption));
        }
        
        reader.close();
        return networks;
    }
    
    private List<WiFiNetwork> getDemoNetworks() {
        List<WiFiNetwork> networks = new ArrayList<>();
        networks.add(new WiFiNetwork("HomeWiFi_5G", "AA:BB:CC:DD:EE:FF", -45, "5 GHz", "WPA2"));
        networks.add(new WiFiNetwork("Office_Network", "11:22:33:44:55:66", -60, "2.4 GHz", "WPA3"));
        networks.add(new WiFiNetwork("Guest_WiFi", "FF:EE:DD:CC:BB:AA", -75, "2.4 GHz", "WPA2"));
        networks.add(new WiFiNetwork("Cafe_Free_WiFi", "12:34:56:78:90:AB", -80, "2.4 GHz", "Open"));
        networks.add(new WiFiNetwork("Neighbor_Network", "AB:CD:EF:12:34:56", -85, "2.4 GHz", "WPA2"));
        return networks;
    }
}