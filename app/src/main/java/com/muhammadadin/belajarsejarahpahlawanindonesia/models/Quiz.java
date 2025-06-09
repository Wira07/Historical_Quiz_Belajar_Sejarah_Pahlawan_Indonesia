package com.muhammadadin.belajarsejarahpahlawanindonesia.models;

import com.google.firebase.firestore.PropertyName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Quiz {
    private String id;
    private String question;
    private List<String> options; // CHANGED: Use List<String> instead of String[]
    private int correctAnswer; // Index of the correct option
    private String heroId;
    private long timestamp;

    // Default constructor for Firestore (REQUIRED)
    public Quiz() {
        this.options = new ArrayList<>(); // Initialize as empty list
        this.heroId = "";
        this.timestamp = System.currentTimeMillis();
    }

    // Constructor used to create a new quiz (accepting String[] for convenience)
    public Quiz(String question, String[] options, int correctAnswer) {
        this.question = question;
        this.options = options != null ? Arrays.asList(options) : new ArrayList<>();
        this.correctAnswer = correctAnswer;
        this.heroId = "";
        this.timestamp = System.currentTimeMillis();
    }

    // Constructor accepting List<String>
    public Quiz(String question, List<String> options, int correctAnswer) {
        this.question = question;
        this.options = options != null ? new ArrayList<>(options) : new ArrayList<>();
        this.correctAnswer = correctAnswer;
        this.heroId = "";
        this.timestamp = System.currentTimeMillis();
    }

    // Full constructor
    public Quiz(String id, String question, List<String> options, int correctAnswer, String heroId) {
        this.id = id;
        this.question = question;
        this.options = options != null ? new ArrayList<>(options) : new ArrayList<>();
        this.correctAnswer = correctAnswer;
        this.heroId = heroId != null ? heroId : "";
        this.timestamp = System.currentTimeMillis();
    }

    // Getters and Setters with Firestore annotations

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @PropertyName("question")
    public String getQuestion() {
        return question;
    }

    @PropertyName("question")
    public void setQuestion(String question) {
        this.question = question;
    }

    // PRIMARY: Use List<String> for Firestore compatibility
    @PropertyName("options")
    public List<String> getOptions() {
        return options != null ? options : new ArrayList<>();
    }

    @PropertyName("options")
    public void setOptions(List<String> options) {
        this.options = options != null ? new ArrayList<>(options) : new ArrayList<>();
    }

    // CONVENIENCE: Array version for backward compatibility
    public String[] getOptionsAsArray() {
        if (options != null && !options.isEmpty()) {
            return options.toArray(new String[0]);
        }
        return new String[0];
    }

    public void setOptionsFromArray(String[] optionsArray) {
        if (optionsArray != null) {
            this.options = new ArrayList<>(Arrays.asList(optionsArray));
        } else {
            this.options = new ArrayList<>();
        }
    }

    @PropertyName("correctAnswer")
    public int getCorrectAnswer() {
        return correctAnswer;
    }

    @PropertyName("correctAnswer")
    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    @PropertyName("heroId")
    public String getHeroId() {
        return heroId;
    }

    @PropertyName("heroId")
    public void setHeroId(String heroId) {
        this.heroId = heroId != null ? heroId : "";
    }

    @PropertyName("timestamp")
    public long getTimestamp() {
        return timestamp;
    }

    @PropertyName("timestamp")
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    // Utility methods for validation
    public boolean hasValidQuestion() {
        return question != null && !question.trim().isEmpty();
    }

    public boolean hasValidOptions() {
        return options != null && options.size() >= 2;
    }

    public boolean hasValidCorrectAnswer() {
        return correctAnswer >= 0 && options != null && correctAnswer < options.size();
    }

    public boolean isValid() {
        return hasValidQuestion() && hasValidOptions() && hasValidCorrectAnswer();
    }

    public String getCorrectAnswerText() {
        if (hasValidCorrectAnswer()) {
            return options.get(correctAnswer);
        }
        return "Invalid";
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "id='" + id + '\'' +
                ", question='" + question + '\'' +
                ", options=" + options +
                ", correctAnswer=" + correctAnswer +
                ", heroId='" + heroId + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Quiz quiz = (Quiz) o;

        if (id != null) {
            return id.equals(quiz.id);
        } else {
            return question != null && question.equals(quiz.question);
        }
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : (question != null ? question.hashCode() : 0);
    }
}