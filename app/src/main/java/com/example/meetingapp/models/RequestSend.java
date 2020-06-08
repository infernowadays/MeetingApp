package com.example.meetingapp.models;

import com.google.gson.annotations.SerializedName;

public class RequestSend {

    @SerializedName("to_user")
    private String toUser;

    @SerializedName("event")
    private long event;

    public RequestSend(String toUser, long event) {
        this.toUser = toUser;
        this.event = event;
    }

    public RequestSend() {
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public long getEvent() {
        return event;
    }

    public void setEvent(long event) {
        this.event = event;
    }
}
