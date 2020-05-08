package com.example.meetingapp.models;

import android.content.Intent;

import com.google.gson.annotations.SerializedName;

public class Message {
    @SerializedName("from_user")
    private UserProfile fromUser;
    private String text;
    private String created;
    private boolean seen;
    private int event;

    public Message(String text, String created, boolean seen, int event) {
        this.text = text;
        this.created = created;
        this.seen = seen;
        this.event = event;
    }


    public Message(UserProfile fromUser, String text, String created, boolean seen) {
        this.fromUser = fromUser;
        this.text = text;
        this.created = created;
        this.seen = seen;
    }

    public Message() {
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

    public int getEvent() {
        return event;
    }

    public void setEvent(int event) {
        this.event = event;
    }
}
