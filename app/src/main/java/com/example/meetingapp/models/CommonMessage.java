package com.example.meetingapp.models;

import com.google.gson.annotations.SerializedName;

public class CommonMessage {
    @SerializedName("from_user")
    private UserProfile fromUser;
    private String text;
    private String created;
    private boolean seen;

    public CommonMessage(String text, String created, boolean seen) {
        this.text = text;
        this.created = created;
        this.seen = seen;
    }

    public CommonMessage(UserProfile fromUser, String text, String created, boolean seen) {
        this.fromUser = fromUser;
        this.text = text;
        this.created = created;
        this.seen = seen;
    }

    public CommonMessage() {
    }

    public UserProfile getFromUser() {
        return fromUser;
    }

    public void setFromUser(UserProfile fromUser) {
        this.fromUser = fromUser;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public boolean getSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }
}
