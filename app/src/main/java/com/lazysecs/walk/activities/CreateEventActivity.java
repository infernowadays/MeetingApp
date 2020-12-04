package com.lazysecs.walk.activities;

import android.content.Intent;
import android.os.Bundle;

import com.lazysecs.walk.interfaces.EventManager;
import com.lazysecs.walk.adapters.EventStepperAdapter;
import com.lazysecs.walk.models.Event;

public class CreateEventActivity extends BaseCreateContentActivity implements EventManager {

    @Override
    public void setupAdapter() {
        EventStepperAdapter eventStepperAdapter = new EventStepperAdapter(getSupportFragmentManager(), this);
        stepperLayout.setAdapter(eventStepperAdapter);
    }

    @Override
    public void loadContent() {
        Intent intent = getIntent();
        this.saveAction(intent.getStringExtra("action"));

        if (intent.hasExtra("EXTRA_EVENT")) {
            Event event = intent.getParcelableExtra("EXTRA_EVENT");
            if (event != null) {
                this.saveId(event.getId());
                this.saveDescription(event.getDescription());
                this.saveDate(event.getDate());
                this.saveTime(event.getTime());
                this.saveLocation(event.getGeoPoint());
            }
        }
    }

    @Override
    public void setupAdditionalOnSaveInstanceState(Bundle outState) {

    }
}