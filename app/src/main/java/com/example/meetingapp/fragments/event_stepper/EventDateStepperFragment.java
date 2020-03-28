package com.example.meetingapp.fragments.event_stepper;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.meetingapp.EventManager;
import com.example.meetingapp.R;
import com.google.android.material.button.MaterialButton;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.Calendar;
import java.util.Objects;


public class EventDateStepperFragment extends Fragment implements BlockingStep, DatePickerDialog.OnDateSetListener {

    private MaterialEditText setDate;
    private MaterialEditText setTimeOptional;
    private EventManager eventManager;

    public static EventDateStepperFragment newInstance() {
        return new EventDateStepperFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_event_date_stepper, container, false);

        setDate = view.findViewById(R.id.setDate);
        setDate.setOnClickListener(v -> {
            showDatePickerDialog();
        });


        setTimeOptional = view.findViewById(R.id.setTimeOptional);
        setTimeOptional.setOnClickListener(v -> {
            showTimePickerDialog();
        });

        return view;
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                Objects.requireNonNull(getActivity()),
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @SuppressLint("SetTextI18n")
    private void showTimePickerDialog(){
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), (view1, hour, minute) -> {
            String strMinute = String.valueOf(minute);
            if (minute < 10) {
                strMinute = "0" + minute;
            }

            String strHour = String.valueOf(hour);
            if (hour < 10) {
                strHour = "0" + hour;
            }
            setTimeOptional.setText(strHour + ":" + strMinute);
        }, 0, 0, true);
        timePickerDialog.show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        String strMonth = String.valueOf(month);
        if (month < 10) {
            strMonth = "0" + month;
        }

        String strDay = String.valueOf(day);
        if (day < 10) {
            strDay = "0" + day;
        }

        setDate.setText(year + "-" + strMonth + "-" + strDay);
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
