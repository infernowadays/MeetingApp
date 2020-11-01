package com.example.meetingapp.models;

import com.google.gson.annotations.SerializedName;

public class Chat {
    @SerializedName("content_type")
    private String contentType;
    @SerializedName("content_id")
    private int contentId;
    @SerializedName("last_message")
    private String lastMessage;
    @SerializedName("from_user")
    private UserProfile fromUser;
    @SerializedName("title")
    private String title;

    public Chat(String contentType, int contentId, String lastMessage, UserProfile fromUser, String title) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.lastMessage = lastMessage;
        this.fromUser = fromUser;
        this.title = title;
    }

    public UserProfile getFromUser() {
        return fromUser;
    }

    public void setFromUser(UserProfile fromUser) {
        this.fromUser = fromUser;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
