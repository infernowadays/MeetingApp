package com.example.meetingapp.models;

public class RegisterData extends LoginData {
    private String username;

    public RegisterData(String email, String username, String password) {
        super(email, password);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
