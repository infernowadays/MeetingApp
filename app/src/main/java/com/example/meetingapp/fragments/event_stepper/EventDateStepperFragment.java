package com.example.meetingapp.fragments.event_stepper;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.meetingapp.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;


public class EventDateStepperFragment extends Fragment implements Step {

    private MaterialButton setDate;
    private MaterialButton setTimeOptional;
    private TimePickerDialog timePickerDialog;

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

    @Nullable
    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }
}
