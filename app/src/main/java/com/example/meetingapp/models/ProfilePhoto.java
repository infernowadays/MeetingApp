package com.example.meetingapp.models;

public class ProfilePhoto {
    private String photo;

    public ProfilePhoto(String photo) {
        this.photo = photo;
    }

    public ProfilePhoto() {

    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
