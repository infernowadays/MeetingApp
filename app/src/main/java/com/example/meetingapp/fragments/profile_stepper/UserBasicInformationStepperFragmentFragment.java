package com.example.meetingapp.fragments.profile_stepper;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.meetingapp.IUserProfileManager;
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


public class UserBasicInformationStepperFragmentFragment extends Fragment implements BlockingStep, DatePickerDialog.OnDateSetListener {


    @BindView(R.id.text_birth_date)
    MaterialEditText textBirthDate;

    private IUserProfileManager iUserProfileManager;


    private String pattern = "yyyy-MM-dd";
    private Date date;

    public static UserBasicInformationStepperFragmentFragment newInstance() {
        return new UserBasicInformationStepperFragmentFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_info_stepper_fragment, container, false);
        ButterKnife.bind(this, view);

        date = new Date();

        return view;
    }

    @OnClick(R.id.text_birth_date)
    void setTextDate() {
        showDatePickerDialog();
    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        String date = Objects.requireNonNull(textBirthDate.getText()).toString();
        iUserProfileManager.saveBirthDate(date);

        callback.goToNextStep();
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();
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

    private void showDatePickerDialog() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireActivity(),
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
        datePickerDialog.show();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IUserProfileManager) {
            iUserProfileManager = (IUserProfileManager) context;
        } else {
            throw new IllegalStateException("Activity must implement IUserProfileManager interface!");
        }
    }

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
        textBirthDate.setText(dateString);
        setDateFromString(dateString);
    }

    private void setDateFromString(String dateString) {
        DateFormat format = new SimpleDateFormat(pattern, Locale.ENGLISH);
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
