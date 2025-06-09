package com.muhammadadin.belajarsejarahpahlawanindonesia.models;

import com.google.firebase.firestore.PropertyName;

public class Hero {
    private String id;
    private String name;
    private String description;
    private String imageUrl;
    private boolean isUnlocked;

    // Default constructor required for Firestore
    public Hero() {
        this.isUnlocked = false; // Default value
    }

    // Constructor with parameters
    public Hero(String name, String description, String imageUrl) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.isUnlocked = false; // Default value
    }

    // Full constructor
    public Hero(String id, String name, String description, String imageUrl, boolean isUnlocked) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.isUnlocked = isUnlocked;
    }

    // Getters and Setters with proper annotations for Firestore

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @PropertyName("name")
    public String getName() {
        return name;
    }

    @PropertyName("name")
    public void setName(String name) {
        this.name = name;
    }

    @PropertyName("description")
    public String getDescription() {
        return description;
    }

    @PropertyName("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @PropertyName("imageUrl")
    public String getImageUrl() {
        return imageUrl;
    }

    @PropertyName("imageUrl")
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @PropertyName("isUnlocked")
    public boolean isUnlocked() {
        return isUnlocked;
    }

    @PropertyName("isUnlocked")
    public void setUnlocked(boolean unlocked) {
        isUnlocked = unlocked;
    }

    // Utility methods

    public boolean hasValidImageUrl() {
        return imageUrl != null && !imageUrl.trim().isEmpty() &&
                (imageUrl.startsWith("http://") || imageUrl.startsWith("https://"));
    }

    public boolean hasValidName() {
        return name != null && !name.trim().isEmpty();
    }

    public boolean hasValidDescription() {
        return description != null && !description.trim().isEmpty();
    }

    @Override
    public String toString() {
        return "Hero{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", isUnlocked=" + isUnlocked +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Hero hero = (Hero) o;

        if (id != null) {
            return id.equals(hero.id);
        } else {
            return name != null && name.equals(hero.name);
        }
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : (name != null ? name.hashCode() : 0);
    }
}