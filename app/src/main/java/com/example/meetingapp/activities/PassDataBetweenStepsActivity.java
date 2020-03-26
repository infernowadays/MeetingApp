package com.example.meetingapp.activities;

import android.app.Fragment;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.meetingapp.DataManager;
import com.example.meetingapp.R;
import com.example.meetingapp.adapters.StepperAdapter;
import com.stepstone.stepper.StepperLayout;

public class PassDataBetweenStepsActivity extends AppCompatActivity implements DataManager {

    private static final String CURRENT_STEP_POSITION_KEY = "position";
    private static final String DATA = "data";

    private StepperLayout mStepperLayout;

    private String mData;

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
        outState.putString(DATA, mData);
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
    public void saveData(String data) {
        mData = data;
    }

    public String getData() {
        return mData;
    }
}