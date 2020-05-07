package com.example.meetingapp;

import com.example.meetingapp.models.GeoPoint;

public interface EventManager {

    void saveAction(String action);

    String getAction();

    void saveId(int id);

    int getId();

    void saveDescription(String description);

    String getDescription();

    void saveDate(String date);

    String getDate();

    void saveTime(String time);

    String getTime();

    void saveLocation(GeoPoint location);

    GeoPoint getLocation();

}
