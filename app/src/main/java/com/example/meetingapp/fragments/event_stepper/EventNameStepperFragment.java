package com.example.meetingapp.fragments.event_stepper;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.meetingapp.EventManager;
import com.example.meetingapp.R;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;


public class EventNameStepperFragment extends Fragment implements BlockingStep {

    private EventManager eventManager;
    private MaterialEditText editEventName;

    public static EventNameStepperFragment newInstance() {
        return new EventNameStepperFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String string = "2020-03-27T16:43:32.749945Z";
        String defaultTimezone = TimeZone.getDefault().getID();
        Date date = null;
        try {
            date = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ")).parse(string.replaceAll("Z$", "+0000"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat dateFormat = DateFormat.getTimeInstance(DateFormat.DEFAULT);
        String d = dateFormat.format(date);


        View view = inflater.inflate(R.layout.fragment_event_name_stepper, container, false);
        editEventName = view.findViewById(R.id.event_name_creation);

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
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
        if(editEventName.getText().toString().matches("")){
            Toast.makeText(getActivity(), "Напишите описание )", Toast.LENGTH_SHORT).show();

            return new VerificationError("empty name");
        }
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        String name = Objects.requireNonNull(editEventName.getText()).toString();
        eventManager.saveDescription(name);

        callback.goToNextStep();
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();
    }
}
