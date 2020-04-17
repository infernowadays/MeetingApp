package com.example.meetingapp.models;

import com.google.gson.annotations.SerializedName;

public class EventRequest {

    @SerializedName("from_user")
    private String fromUser;

    @SerializedName("to_user")
    private String toUser;

    private long event;
    private String decision;
    private boolean seen;

    public EventRequest(String fromUser, String toUser, long event) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.event = event;
    }

    public EventRequest() {
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
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
