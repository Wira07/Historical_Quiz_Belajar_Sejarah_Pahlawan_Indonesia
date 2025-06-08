package com.muhammadadin.belajarsejarahpahlawanindonesia.models;

public class Fact {
    private String id;
    private String heroId;
    private String title;
    private String content;
    private boolean isDiscovered;

    public Fact() {} // Required for Firebase

    public Fact(String id, String heroId, String title, String content) {
        this.id = id;
        this.heroId = heroId;
        this.title = title;
        this.content = content;
        this.isDiscovered = false;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getHeroId() { return heroId; }
    public void setHeroId(String heroId) { this.heroId = heroId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public boolean isDiscovered() { return isDiscovered; }
    public void setDiscovered(boolean discovered) { isDiscovered = discovered; }
}
