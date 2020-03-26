package com.example.meetingapp.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.example.meetingapp.activities.StepperActivity;
import com.example.meetingapp.fragments.event_stepper.EventDateStepperFragment;
import com.example.meetingapp.fragments.event_stepper.EventGeoLocationStepperFragment;
import com.example.meetingapp.fragments.event_stepper.EventNameStepperFragment;
import com.example.meetingapp.fragments.event_stepper.EventPublishStepperFragment;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;

public class StepperAdapter extends AbstractFragmentStepAdapter {

    private static final String CURRENT_STEP_POSITION_KEY = "messageResourceId";

    public StepperAdapter(FragmentManager fm, StepperActivity context) {
        super(fm, context);
    }

    public StepperAdapter(@NonNull FragmentManager fm, @NonNull Context context) {
        super(fm, context);
    }

    @Override
    public Step createStep(int position) {
        switch (position) {
            case 0:
                return EventNameStepperFragment.newInstance();

            case 1:
                return EventDateStepperFragment.newInstance();

            case 2:
                return EventGeoLocationStepperFragment.newInstance();

            case 3:
                return EventPublishStepperFragment.newInstance();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }

//    @NonNull
//    @Override
//    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
//
//        switch (position) {
//            case 0:
//                return new StepViewModel.Builder(context)
//                        .setTitle("Tabs 1") //can be a CharSequence instead
//                        .create();
//            case 1:
//                return new StepViewModel.Builder(context)
//                        .setTitle("Tabs 2") //can be a CharSequence instead
//                        .create();
//        }
//        return null;
//    }


//        //Override this method to set Step title for the Tabs, not necessary for other stepper types
//        return new StepViewModel.Builder(context)
//                .setTitle("Create Event") //can be a CharSequence instead
//                .create();
}