package com.example.meetingapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Login {
    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("password")
    @Expose
    private  String password;

    public Login(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
