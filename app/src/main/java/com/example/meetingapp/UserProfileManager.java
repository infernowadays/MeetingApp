package com.example.meetingapp;

import com.example.meetingapp.models.UserProfile;


public class UserProfileManager {
    private static UserProfileManager instance;
    private static UserProfile myProfile = null;

    private UserProfileManager() {
    }

    public static UserProfile getMyProfile() {
        return myProfile;
    }

    public static UserProfileManager getInstance(UserProfile userProfile) {
        if (instance == null) {
            instance = new UserProfileManager();
            myProfile = userProfile;
        }

        return instance;
    }
}
