package com.example.meetingapp.models;

public class Message {
    private String uid;
    private Long event_id;
    private String message;
    private String date;
    private String time;

    public Message(String uid, Long event_id, String message, String date) {
        this.uid = uid;
        this.event_id = event_id;
        this.message = message;
        this.date = date;
    }

    public Message() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Long getEvent_id() {
        return event_id;
    }

    public void setEvent_id(Long event_id) {
        this.event_id = event_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
