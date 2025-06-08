package com.muhammadadin.belajarsejarahpahlawanindonesia.models;

public class User {
    private String uid;
    private String email;
    private String name;
    private String role; // "admin" or "student"
    private int points;
    private int level;

    public User() {} // Required for Firebase

    public User(String uid, String email, String name, String role) {
        this.uid = uid;
        this.email = email;
        this.name = name;
        this.role = role;
        this.points = 0;
        this.level = 1;
    }

    // Getters and Setters
    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
}