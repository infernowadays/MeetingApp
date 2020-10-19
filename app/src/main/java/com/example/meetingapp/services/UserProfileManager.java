package com.example.meetingapp.services;

import com.example.meetingapp.models.UserProfile;


public class UserProfileManager {
    private static UserProfileManager instance;
    private static UserProfile myProfile = null;

    private UserProfileManager() {
    }

    public static UserProfileManager getInstance() {
        if (instance == null)
            instance = new UserProfileManager();

        return instance;
    }

    public UserProfile getMyProfile() {
        return myProfile;
    }

    public void initialize(UserProfile userProfile) {
        myProfile = userProfile;
    }
}
