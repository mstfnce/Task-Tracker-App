package com.firstapp.hedeftakipuygulamasi;

import java.io.Serializable;

public class Task implements Serializable {
    private String title;
    private boolean isCompleted;
    private String date;
    private String target;
    private String category;
    private String description;
    private String completedAmount;

    public Task(String title, boolean isCompleted, String date, String target, String category, String description, String completedTarget) {
        this.title = title;
        this.isCompleted = isCompleted;
        this.date = date;
        this.target = target;
        this.category = category;
        this.description = description;
        this.completedAmount = completedTarget;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompletedAmount() {
        return completedAmount;
    }

    public void setCompletedAmount(String completedAmount) {
        this.completedAmount = completedAmount;
    }
}

