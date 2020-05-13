package com.example.meetingapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MegaCategory {
    private String name;
    private List<Category> categories;

    public MegaCategory(String name, List<Category> categories) {
        this.name = name;
        this.categories = categories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
