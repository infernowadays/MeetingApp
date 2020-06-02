package com.example.meetingapp.models;

import com.google.gson.annotations.SerializedName;

public class Complaint {
    @SerializedName("user_profile")
    private UserProfile userProfile;

    @SerializedName("message")
    private String message;
    @SerializedName("content_id")
    private int contentId;
    @SerializedName("content_type")
    private String contentType;

    public Complaint(UserProfile userProfile, String message, int contentId, String contentType) {
        this.userProfile = userProfile;
        this.message = message;
        this.contentId = contentId;
        this.contentType = contentType;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
