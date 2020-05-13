package com.example.meetingapp;

import java.util.ArrayList;

public interface IUserProfileManager {

    void saveSex(String sex);

    String getSex();

    void savePhotoUrl(String photoUrl);

    String getPhotoUrl();

    void saveBirthDate(String birthDate);

    String getBirthDate();

    void saveCity(String city);

    String getCity();

    void saveEducation(String education);

    String getEducation();

    void saveJob(String job);

    String getJob();

    void saveCategories(ArrayList<String> categories);

    ArrayList<String> getCategories();
}
