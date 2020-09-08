package com.example.todoapp;

import com.google.firebase.Timestamp;

public class Note {

    private String text;
    private Boolean completed;
    private String userId;
    private Timestamp created;
    public Note(){

    }
    public Note(String text,Boolean completed,String userId,Timestamp created){
        this.text = text;
        this.completed = completed;
        this.userId = userId;
        this.created = created;

    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "Note{" +
                "text='" + text + '\'' +
                ", completed=" + completed +
                ", userId='" + userId + '\'' +
                ", created=" + created +
                '}';
    }
}

