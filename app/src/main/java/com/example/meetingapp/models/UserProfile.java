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
    @SerializedName("token")
    private String token;
    @SerializedName("username")
    private String username;
    @SerializedName("email")
    private String email;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("city")
    private String city;
    @SerializedName("education")
    private String education;
    @SerializedName("job")
    private String job;
    @SerializedName("photo")
    private ProfilePhoto photo;
    @SerializedName("sex")
    private String sex;
    @SerializedName("categories")
    private List<Category> categories;
    @SerializedName("is_confirmed")
    private Boolean confirmed;
    @SerializedName("is_filled")
    private Boolean filled;

    public UserProfile(int id,
                       String firebaseUid,
                       String dateOfBirth,
                       String token,
                       String username,
                       String email,
                       String firstName,
                       String lastName,
                       String city,
                       String education,
                       String job,
                       ProfilePhoto photo,
                       String sex,
                       List<Category> categories,
                       Boolean confirmed,
                       Boolean filled) {
        this.id = id;
        this.firebaseUid = firebaseUid;
        this.dateOfBirth = dateOfBirth;
        this.token = token;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.education = education;
        this.job = job;
        this.photo = photo;
        this.sex = sex;
        this.categories = categories;
        this.confirmed = confirmed;
        this.filled = filled;
    }

    public UserProfile(int id) {
        this.id = id;
    }

    public UserProfile() {
    }

    public Boolean getFilled() {
        return filled;
    }

    public void setFilled(Boolean filled) {
        this.filled = filled;
    }

    public Boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}