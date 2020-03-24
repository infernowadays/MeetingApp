package com.example.meetingapp.adapters;

import android.content.Context;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.example.meetingapp.activities.PassDataBetweenStepsActivity;
import com.example.meetingapp.activities.StepperActivity;
import com.example.meetingapp.fragments.CreateEventByStep2Fragment;
import com.example.meetingapp.fragments.CreateEventByStepFragment;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

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
                return CreateEventByStepFragment.newInstance();
//                final CreateEventByStepFragment step1 = new CreateEventByStepFragment();
//                Bundle b1 = new Bundle();
//                b1.putInt(CURRENT_STEP_POSITION_KEY, position);
//                step1.setArguments(b1);
//                return step1;
            case 1:
                return CreateEventByStep2Fragment.newInstance();
//                final CreateEventByStep2Fragment step2 = new CreateEventByStep2Fragment();
//                Bundle b2 = new Bundle();
//                b2.putInt(CURRENT_STEP_POSITION_KEY, position);
//                step2.setArguments(b2);
//                return step2;
        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
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