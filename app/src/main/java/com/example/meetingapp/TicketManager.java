package com.example.meetingapp;

import java.util.ArrayList;

public interface TicketManager {

    void saveName(String name);

    String getName();

    void saveDescription(String description);

    String getDescription();

    void savePrice(Double price);

    Double getPrice();

    void saveDate(String date);

    String getDate();

    void saveCategories(ArrayList<String> categories);

    ArrayList<String> getCategories();
}
