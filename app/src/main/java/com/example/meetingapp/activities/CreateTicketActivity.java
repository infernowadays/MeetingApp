package com.example.meetingapp.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.meetingapp.R;
import com.example.meetingapp.TicketManager;
import com.example.meetingapp.adapters.TicketStepperAdapter;
import com.stepstone.stepper.StepperLayout;

import java.util.ArrayList;

public class CreateTicketActivity extends AppCompatActivity implements TicketManager {

    private static final String CURRENT_STEP_POSITION_KEY = "position";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String PRICE = "price";
    private static final String DATE = "date";
    private static final String CATEGORIES = "categories";
    private static final String ACTION = "action";

    private StepperLayout stepperLayout;

    private String action;
    private String name;
    private String description;
    private int price;
    private String date;
    private ArrayList<String> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step);

        stepperLayout = findViewById(R.id.stepperLayout);
        TicketStepperAdapter ticketStepperAdapter = new TicketStepperAdapter(getSupportFragmentManager(), this);
        stepperLayout.setAdapter(ticketStepperAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_STEP_POSITION_KEY, stepperLayout.getCurrentStepPosition());
        outState.putString(NAME, name);
        outState.putString(DESCRIPTION, description);
        outState.putDouble(PRICE, price);
        outState.putString(DATE, date);
        outState.putStringArrayList(CATEGORIES, categories);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        final int currentStepPosition = stepperLayout.getCurrentStepPosition();
        if (currentStepPosition > 0) {
            stepperLayout.onBackClicked();
        } else {
            finish();
        }
    }

    @Override
    public void saveName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void saveDescription(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void savePrice(int price) {
        this.price = price;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public void saveDate(String date) {
        this.date = date;
    }

    @Override
    public String getDate() {
        return date;
    }

    @Override
    public void saveCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    @Override
    public ArrayList<String> getCategories() {
        return categories;
    }
}
