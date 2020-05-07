package com.example.meetingapp.models;

import java.util.List;

public class Chat {
    private List<UserProfile> members;

    public Chat(List<UserProfile> members) {
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
}
