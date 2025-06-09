package com.muhammadadin.belajarsejarahpahlawanindonesia.models;

import com.google.firebase.firestore.PropertyName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Quiz {
    private String id;
    private String question;
    private String[] options;
    private int correctAnswer; // Index of the correct option
    private String heroId;
    private long timestamp;

    // Default constructor for Firestore (REQUIRED)
    public Quiz() {
        this.heroId = "";
        this.timestamp = System.currentTimeMillis();
    }

    // Constructor used to create a new quiz
    public Quiz(String question, String[] options, int correctAnswer) {
        this.question = question;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.heroId = "";
        this.timestamp = System.currentTimeMillis();
    }

    // Full constructor
    public Quiz(String id, String question, String[] options, int correctAnswer, String heroId) {
        this.id = id;
        this.question = question;
        this.options = options;
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

    // FIXED: Use List<String> for Firestore compatibility instead of String[]
    @PropertyName("options")
    public List<String> getOptionsAsList() {
        if (options != null) {
            return Arrays.asList(options);
        }
        return new ArrayList<>();
    }

    @PropertyName("options")
    public void setOptionsAsList(List<String> optionsList) {
        if (optionsList != null) {
            this.options = optionsList.toArray(new String[0]);
        } else {
            this.options = new String[0];
        }
    }

    // Keep the array version for backward compatibility
    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
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
        return options != null && options.length >= 2;
    }

    public boolean hasValidCorrectAnswer() {
        return correctAnswer >= 0 && options != null && correctAnswer < options.length;
    }

    public boolean isValid() {
        return hasValidQuestion() && hasValidOptions() && hasValidCorrectAnswer();
    }

    public String getCorrectAnswerText() {
        if (hasValidCorrectAnswer()) {
            return options[correctAnswer];
        }
        return "Invalid";
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "id='" + id + '\'' +
                ", question='" + question + '\'' +
                ", options=" + (options != null ? Arrays.toString(options) : null) +
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