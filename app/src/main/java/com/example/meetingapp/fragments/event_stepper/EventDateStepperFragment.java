package com.example.meetingapp.fragments.event_stepper;

import android.app.TimePickerDialog;
import android.content.Context;
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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;


public class EventDateStepperFragment extends Fragment implements BlockingStep {

    private MaterialButton setDate;
    private MaterialButton setTimeOptional;
    private TimePickerDialog timePickerDialog;
    private EventManager eventManager;

    public static EventDateStepperFragment newInstance() {
        return new EventDateStepperFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_event_date_stepper, container, false);

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Выберите дату");
        final MaterialDatePicker materialDatePicker = builder.build();

        setDate = view.findViewById(R.id.setDate);
        setDate.setOnClickListener(v -> {
            materialDatePicker.show(getChildFragmentManager(), "DATE_PICKER");
            materialDatePicker.addOnPositiveButtonClickListener(selection -> {
                setDate.setText(materialDatePicker.getHeaderText());
            });
        });


        setTimeOptional = view.findViewById(R.id.setTimeOptional);
        setTimeOptional.setOnClickListener(v -> {
            timePickerDialog = new TimePickerDialog(getActivity(), (view1, hourOfDay, minute) -> {
                setTimeOptional.setText(hourOfDay + ":" + minute);
            }, 0, 0, true);
            timePickerDialog.show();
        });

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
        if (setDate.getText().toString().matches("")) {
            Toast.makeText(getActivity(), "Укажите дату )", Toast.LENGTH_SHORT).show();

            return new VerificationError("empty date");
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
        String date = setDate.getText().toString();
        eventManager.saveDate(date);

        String timeOptional = setTimeOptional.getText().toString();
        eventManager.saveTime(timeOptional);

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
