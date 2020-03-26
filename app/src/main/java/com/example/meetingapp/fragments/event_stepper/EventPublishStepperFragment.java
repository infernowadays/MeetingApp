package com.example.meetingapp.fragments.event_stepper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.meetingapp.EventManager;
import com.example.meetingapp.R;
import com.example.meetingapp.activities.BottomNavigationActivity;
import com.google.android.material.textview.MaterialTextView;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;


public class EventPublishStepperFragment extends Fragment implements BlockingStep {

    private EventManager eventManager;
    private MaterialTextView description;
    private MaterialTextView date;
    private MaterialTextView time;
    private MaterialTextView address;

    public static EventPublishStepperFragment newInstance() {
        return new EventPublishStepperFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_publish_stepper, container, false);

        description = view.findViewById(R.id.create_event_description);
        date = view.findViewById(R.id._create_event_date);
        time = view.findViewById(R.id.create_event_time);
        address = view.findViewById(R.id.create_event_address);


        return view;
    }

    @Override
    public void onAttach(@androidx.annotation.NonNull Context context) {
        super.onAttach(context);
        if (context instanceof EventManager) {
            eventManager = (EventManager) context;
        } else {
            throw new IllegalStateException("Activity must implement EventManager interface!");
        }
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {
        description.setText(eventManager.getDescription());
        date.setText(eventManager.getDate());
        time.setText(eventManager.getTime());
        address.setText(eventManager.getLocation().getAddress());
    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {

    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {
        Toast.makeText(getActivity(), "Событие создано!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), BottomNavigationActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();
    }
}
