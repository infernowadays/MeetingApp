package com.example.meetingapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserProfile {

    @SerializedName("id")
    private int id;
    @SerializedName("firebase_uid")
    private String firebaseUid;
    @SerializedName("date_of_birth")
    private String dateOfBirth;
    @SerializedName("firebase_registration_token")
    private String firebaseToken;
    @SerializedName("token")
    private String token;
    @SerializedName("username")
    private String username;
    @SerializedName("email")
    private String email;
    @SerializedName("photo")
    private ProfilePhoto photo;
    @SerializedName("sex")
    private String sex;
    @SerializedName("categories")
    private List<Category> categories;

    public UserProfile(String firebaseUid, String dateOfBirth, int id, String username, String email, ProfilePhoto photo, String sex, List<Category> categories) {
        this.firebaseUid = firebaseUid;
        this.dateOfBirth = dateOfBirth;
        this.id = id;
        this.username = username;
        this.email = email;
        this.photo = photo;
        this.sex = sex;
        this.categories = categories;
    }

    public UserProfile(int id){
        this.id = id;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public ProfilePhoto getPhoto() {
        return photo;
    }

    public void setPhoto(ProfilePhoto photo) {
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

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}