package com.example.meetingapp.models;

import java.util.List;

public class Ticket {
    private int id;
    private String name;
    private String address;
    private int price;
    private UserProfile creator;
    private String created;
    private String date;
    private String time;
    private String description;
    private boolean sold;
    private List<Category> categories;

    public Ticket(int id, String name, String address, int price, UserProfile creator, String created, String date, String time, String description, boolean sold, List<Category> categories) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.price = price;
        this.creator = creator;
        this.created = created;
        this.date = date;
        this.time = time;
        this.description = description;
        this.sold = sold;
        this.categories = categories;
    }

    public Ticket(String name, int price, String date, String description, List<Category> categories) {
        this.name = name;
        this.price = price;
        this.date = date;
        this.description = description;
        this.categories = categories;
    }

    public Ticket() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public UserProfile getCreator() {
        return creator;
    }

    public void setCreator(UserProfile creator) {
        this.creator = creator;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSold() {
        return sold;
    }

    public void setSold(boolean sold) {
        this.sold = sold;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
