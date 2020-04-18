package com.example.meetingapp.models;

public class Message {
    private String firebaseUid;
    private Long eventId;
    private String message;
    private String date;
    private String time;

    public Message(String firebaseUid, Long eventId, String message, String date) {
        this.firebaseUid = firebaseUid;
        this.eventId = eventId;
        this.message = message;
        this.date = date;
    }

    public Message() {
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

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getFirebaseUid() {
        return firebaseUid;
    }

    public void setFirebaseUid(String firebaseUid) {
        this.firebaseUid = firebaseUid;
    }
}
