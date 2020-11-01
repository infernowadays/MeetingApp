package com.example.meetingapp.models;

public class Message extends CommonMessage {
    private int event;

    public Message(String text, String created, boolean seen, int event) {
        super(text, created, seen);
        this.event = event;
    }

    public Message(UserProfile fromUser, String text, String created, boolean seen) {
        super(fromUser, text, created, seen);
    }

    public Message() {
    }

    public int getEvent() {
        return event;
    }

    public void setEvent(int event) {
        this.event = event;
    }
}
