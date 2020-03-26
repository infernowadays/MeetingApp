package com.example.meetingapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.meetingapp.EventManager;
import com.example.meetingapp.R;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;


public class CreateEventByStep2Fragment extends ButterKnifeFragment implements Step {

//    @BindView(R.id.test_text_2)
    TextView editText;
    private EventManager eventManager;

    public static CreateEventByStep2Fragment newInstance() {
        return new CreateEventByStep2Fragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_event_by_step2, container, false);

        editText = view.findViewById(R.id.test_text_2);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EventManager) {
            eventManager = (EventManager) context;
        } else {
            throw new IllegalStateException("Activity must implement EventManager interface!");
        }
    }

    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {
        editText.setText("Entered text:\n" + eventManager.getDate());
    }

    @Override
    public void onError(@NonNull VerificationError error) {
    }
}
