package com.example.meetingapp.models;

public class EventRequest {
    private String uid;
    private String creator_id;
    private long event_id;
    private String decision;
    private boolean seen;

    public EventRequest(String uid, String creator_id, long event_id, String decision) {
        this.uid = uid;
        this.creator_id = creator_id;
        this.event_id = event_id;
        this.decision = decision;
    }

    public EventRequest() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public long getEvent_id() {
        return event_id;
    }

    public void setEvent_id(long event_id) {
        this.event_id = event_id;
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

    public String getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(String creator_id) {
        this.creator_id = creator_id;
    }
}
