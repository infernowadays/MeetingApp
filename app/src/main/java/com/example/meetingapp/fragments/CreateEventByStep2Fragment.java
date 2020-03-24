package com.example.meetingapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.meetingapp.DataManager;
import com.example.meetingapp.R;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import butterknife.BindView;


public class CreateEventByStep2Fragment extends ButterKnifeFragment implements Step {

//    @BindView(R.id.test_text_2)
    TextView editText;
    private DataManager dataManager;

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
        if (context instanceof DataManager) {
            dataManager = (DataManager) context;
        } else {
            throw new IllegalStateException("Activity must implement DataManager interface!");
        }
    }

    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {
        editText.setText("Entered text:\n" + dataManager.getData());
    }

    @Override
    public void onError(@NonNull VerificationError error) {
    }
}
