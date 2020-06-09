package com.example.meetingapp.models;

import com.google.gson.annotations.SerializedName;

public class RequestSend {

    @SerializedName("to_user")
    private String toUser;

    @SerializedName("event")
    private long event;
    @SerializedName("decision")
    private String decision;

    public RequestSend(String toUser, long event) {
        this.toUser = toUser;
        this.event = event;
    }

    public RequestSend(String decision) {
        this.decision = decision;
    }

    public RequestSend() {
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
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
