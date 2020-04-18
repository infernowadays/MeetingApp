package com.example.meetingapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Event {
    @SerializedName("geo_point")
    private GeoPoint geoPoint;

    private int id;
    private UserProfile creator;
    private String name;
    private String description;
    private String date;
    private String time;
    private List<Category> categories;

    public Event(int id, UserProfile creator, String name, String description, String date, String time, GeoPoint geoPoint, List<Category> categories) {
        this.id = id;
        this.creator = creator;
        this.name = name;
        this.description = description;
        this.date = date;
        this.time = time;
        this.geoPoint = geoPoint;
        this.categories = categories;
    }

    public Event() {
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public UserProfile getCreator() {
        return creator;
    }

    public void setCreator(UserProfile creator) {
        this.creator = creator;
    }
}
