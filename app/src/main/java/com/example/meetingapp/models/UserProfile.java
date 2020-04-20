package com.example.meetingapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserProfile {

    @SerializedName("firebase_uid")
    private String firebaseUid;

    @SerializedName("date_of_birth")
    private String dateOfBirth;

    private String id;
    private String username;
    private String email;
    private String photo;
    private String sex;
    private List<Category> categories;

    public UserProfile(String firebaseUid, String dateOfBirth, String id, String username, String email, String photo, String sex, List<Category> categories) {
        this.firebaseUid = firebaseUid;
        this.dateOfBirth = dateOfBirth;
        this.id = id;
        this.username = username;
        this.email = email;
        this.photo = photo;
        this.sex = sex;
        this.categories = categories;
    }

    public UserProfile() {
    }

    public String getFirebaseUid() {
        return firebaseUid;
    }

    public void setFirebaseUid(String firebaseUid) {
        this.firebaseUid = firebaseUid;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}