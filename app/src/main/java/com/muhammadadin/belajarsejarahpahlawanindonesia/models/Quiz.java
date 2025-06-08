package com.muhammadadin.belajarsejarahpahlawanindonesia.models;

public class Quiz {
    private String id;
    private String question;
    private String[] options;
    private int correctAnswer; // Index of correct option
    private String heroId;

    public Quiz() {} // Required for Firebase

    public Quiz(String id, String question, String[] options, int correctAnswer, String heroId) {
        this.id = id;
        this.question = question;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.heroId = heroId;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public String[] getOptions() { return options; }
    public void setOptions(String[] options) { this.options = options; }

    public int getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(int correctAnswer) { this.correctAnswer = correctAnswer; }

    public String getHeroId() { return heroId; }
    public void setHeroId(String heroId) { this.heroId = heroId; }
}
