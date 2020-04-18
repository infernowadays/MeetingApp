package com.example.meetingapp.fragments.event_stepper;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.meetingapp.EventManager;
import com.example.meetingapp.R;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class EventDateStepperFragment extends Fragment implements BlockingStep, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    @BindView(R.id.button_today)
    RadioButton buttonToday;

    @BindView(R.id.button_tomorrow)
    RadioButton buttonTomorrow;

    @BindView(R.id.text_date)
    MaterialEditText textDate;

    @BindView(R.id.text_time)
    MaterialEditText textTime;

    private EventManager eventManager;
    private String pattern = "yyyy-MM-dd";
    private DateFormat dateFormat;

    public static EventDateStepperFragment newInstance() {
        return new EventDateStepperFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_date_stepper, container, false);
        ButterKnife.bind(this, view);

        defaultDate();

        return view;
    }

    @OnClick(R.id.text_date)
    void setTextDate() {
        showDatePickerDialog();
    }

    @OnClick(R.id.text_time)
    void setTextTime() {
        showTimePickerDialog();
    }

    @OnClick({R.id.button_today, R.id.button_tomorrow})
    void onRadioButtonClicked(RadioButton radioButton) {
        boolean checked = radioButton.isChecked();

        switch (radioButton.getId()) {
            case R.id.button_today:
                if (checked) {
                    buttonToday.setTextColor(Color.RED);
                    buttonTomorrow.setTextColor(Color.BLACK);

                    String dateString = dateFormat.format(new Date());
                    textDate.setText(dateString);
                }
                break;
            case R.id.button_tomorrow:
                if (checked) {
                    buttonToday.setTextColor(Color.BLACK);
                    buttonTomorrow.setTextColor(Color.RED);

                    Date date = new Date();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.add(Calendar.DATE, 1);
                    String dateString = dateFormat.format(calendar.getTime());

                    textDate.setText(dateString);
                }
                break;
        }
    }

    @SuppressLint("SimpleDateFormat")
    private void defaultDate(){
        buttonToday.setTextColor(Color.RED);

        dateFormat = new SimpleDateFormat(pattern);
        String dateString = dateFormat.format(new Date());
        textDate.setText(dateString);
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

        textDate.setText(year + "-" + strMonth + "-" + strDay);
    }

    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                Objects.requireNonNull(getActivity()),
                this,
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onTimeSet(TimePicker view, int hour, int minute) {
        view.setIs24HourView(true);

        String strMinute = String.valueOf(minute);
        if (minute < 10) {
            strMinute = "0" + minute;
        }

        String strHour = String.valueOf(hour);
        if (hour < 10) {
            strHour = "0" + hour;
        }

        textTime.setText(strHour + ":" + strMinute);
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
        if (Objects.requireNonNull(textDate.getText()).toString().matches("")) {
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
        String date = textDate.getText().toString();
        eventManager.saveDate(date);

        String time = textTime.getText().toString();
        eventManager.saveTime(time);

        callback.goToNextStep();
    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

    }
}
