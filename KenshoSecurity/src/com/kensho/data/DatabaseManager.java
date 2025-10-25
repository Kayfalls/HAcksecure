package com.kensho.data;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class DatabaseManager {
    private static final String DATA_FILE = "data/users.dat";
    private Map<String, User> users;
    
    public DatabaseManager() {
        this.users = new HashMap<>();
        loadUsers();
    }
    
    public static class User implements java.io.Serializable {
        private static final long serialVersionUID = 1L;
        private String username;
        private String passwordHash;
        private String salt;
        private String email;

        public User(String username, String passwordHash, String salt, String email) {
            this.username = username;
            this.passwordHash = passwordHash;
            this.salt = salt;
            this.email = email;
        }

        // Getters
        public String getUsername() { return username; }
        public String getPasswordHash() { return passwordHash; }
        public String getSalt() { return salt; }
        public String getEmail() { return email; }
    }
    
    public boolean userExists(String username) {
        return users.containsKey(username);
    }
    
    public boolean createUser(String username, String passwordHash, String salt, String email) {
        if (users.containsKey(username)) {
            return false;
        }
        
        User user = new User(username, passwordHash, salt, email);
        users.put(username, user);
        saveUsers();
        return true;
    }
    
    public User getUser(String username) {
        return users.get(username);
    }
    
    @SuppressWarnings("unchecked")
    private void loadUsers() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            users = (Map<String, User>) ois.readObject();
        } catch (Exception e) {
            System.err.println("Error loading users: " + e.getMessage());
            users = new HashMap<>();
        }
    }
    
    private void saveUsers() {
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
        
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(users);
        } catch (Exception e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }
}