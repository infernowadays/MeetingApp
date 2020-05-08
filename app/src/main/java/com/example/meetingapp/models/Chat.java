package com.example.meetingapp.models;

import java.util.List;

public class Chat {
    private int id;
    private List<UserProfile> members;

    public Chat(int id, List<UserProfile> members)
    {
        this.id = id;
        this.members = members;
    }


    public Chat() {
    }

    public List<UserProfile> getMembers() {
        return members;
    }

    public void setMembers(List<UserProfile> members) {
        this.members = members;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
