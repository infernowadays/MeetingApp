package com.example.meetingapp.activities;

import android.app.Fragment;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.meetingapp.EventManager;
import com.example.meetingapp.R;
import com.example.meetingapp.adapters.StepperAdapter;
import com.example.meetingapp.models.GeoPoint;
import com.stepstone.stepper.StepperLayout;

public class PassEventBetweenStepsActivity extends AppCompatActivity implements EventManager {

    private static final String CURRENT_STEP_POSITION_KEY = "position";
    private static final String DESCRIPTION = "description";
    private static final String DATE = "date";
    private static final String TIME = "time";
    private static final String LOCATION = "location";


    private StepperLayout mStepperLayout;

    private String mDescription;
    private String mDate;
    private String mTime;
    private GeoPoint mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Stepper sample");

        setContentView(R.layout.step);

//        int startingStepPosition = savedInstanceState != null ? savedInstanceState.getInt(CURRENT_STEP_POSITION_KEY) : 0;
//        mData = savedInstanceState != null ? savedInstanceState.getString(DATA) : null;

        mStepperLayout = findViewById(R.id.stepperLayout);
        StepperAdapter mStepperAdapter = new StepperAdapter(getSupportFragmentManager(), this);


        mStepperLayout.setAdapter(mStepperAdapter);
    }

    public Fragment getLatestFragment(){
        int index = getFragmentManager().getBackStackEntryCount() - 1;
        FragmentManager.BackStackEntry backEntry = (FragmentManager.BackStackEntry) getFragmentManager().getBackStackEntryAt(index);
        String tag = backEntry.getName();
        return getFragmentManager().findFragmentByTag(tag);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_STEP_POSITION_KEY, mStepperLayout.getCurrentStepPosition());
        outState.putString(DESCRIPTION, mDescription);
        outState.putString(DATE, mDate);
        outState.putString(TIME, mTime);
        outState.putParcelable(DESCRIPTION, mLocation);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        final int currentStepPosition = mStepperLayout.getCurrentStepPosition();
        if (currentStepPosition > 0) {
            mStepperLayout.onBackClicked();
        } else {
            finish();
        }
    }

    @Override
    public void saveDescription(String description) {
        mDescription = description;
    }

    public String getDescription() {
        return mDescription;
    }

    @Override
    public void saveDate(String date) {
        mDate = date;
    }

    public String getDate() {
        return mDate;
    }

    @Override
    public void saveTime(String time) {
        mTime = time;
    }

    public String getTime() {
        return mTime;
    }

    @Override
    public void saveLocation(GeoPoint location) {
        mLocation = location;
    }

    public GeoPoint getLocation() {
        return mLocation;
    }
}