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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
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
    private Date date;

    public static EventDateStepperFragment newInstance() {
        return new EventDateStepperFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_date_stepper, container, false);
        ButterKnife.bind(this, view);

        Locale locale = getResources().getConfiguration().locale;
        Locale.setDefault(locale);

        date = new Date();
        if (eventManager.getAction().equals("edit"))
            setDateFromString(eventManager.getDate());

        defaultDate(date);

        return view;
    }

    private void setDateFromString(String dateString) {
        DateFormat format = new SimpleDateFormat(pattern, Locale.ENGLISH);
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
                    buttonToday.setTextColor(getResources().getColor(R.color.colorPrimary));
                    buttonTomorrow.setTextColor(Color.BLACK);

                    String dateString = dateFormat.format(new Date());
                    textDate.setText(dateString);
                    setDateFromString(dateString);
                }
                break;
            case R.id.button_tomorrow:
                if (checked) {
                    buttonToday.setTextColor(Color.BLACK);
                    buttonTomorrow.setTextColor(getResources().getColor(R.color.colorPrimary));

                    Date date = new Date();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.add(Calendar.DATE, 1);
                    String dateString = dateFormat.format(calendar.getTime());

                    textDate.setText(dateString);
                    setDateFromString(dateString);
                }
                break;
        }
    }

    @SuppressLint("SimpleDateFormat")
    private void defaultDate(Date date) {
        buttonToday.setTextColor(getResources().getColor(R.color.colorPrimary));

        dateFormat = new SimpleDateFormat(pattern);
        String dateString = dateFormat.format(date);
        textDate.setText(dateString);
    }

    private void showDatePickerDialog() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireActivity(),
                R.style.DialogTheme,
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
        datePickerDialog.show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        String strMonth = String.valueOf(month + 1);
        if (month < 10) {
            strMonth = "0" + (month + 1);
        }

        String strDay = String.valueOf(day);
        if (day < 10) {
            strDay = "0" + day;
        }

        String dateString = year + "-" + strMonth + "-" + strDay;
        textDate.setText(dateString);
        setDateFromString(dateString);

    }

    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireActivity(),
                R.style.DialogTheme,
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
            textDate.setError("Поле является обязательным для заполнения");
            return new VerificationError("Не все обязательные поля заполнены");
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
