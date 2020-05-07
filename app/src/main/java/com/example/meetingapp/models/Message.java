package com.example.meetingapp.models;

public class Message {
    private String fromUser;
    private Long eventId;
    private String text;
    private String created;
    private String seen;
    private Chat chat;

    public Message(String fromUser, Long eventId, String text, String created, String seen, Chat chat) {
        this.fromUser = fromUser;
        this.eventId = eventId;
        this.text = text;
        this.created = created;
        this.seen = seen;
        this.chat = chat;
    }

    public Message() {
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
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

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

}
