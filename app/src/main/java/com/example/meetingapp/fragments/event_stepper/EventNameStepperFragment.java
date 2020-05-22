package com.example.meetingapp.fragments.event_stepper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


public class EventNameStepperFragment extends Fragment implements BlockingStep {

    @BindView(R.id.event_name_creation)
    MaterialEditText editEventName;
    @BindView(R.id.header_h4)
    TextView headerH4;
    private EventManager eventManager;

    public static EventNameStepperFragment newInstance() {
        return new EventNameStepperFragment();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_name_stepper, container, false);
        ButterKnife.bind(this, view);

        int unicode = 0x1F60A;
        headerH4.setText("Опишите событие максимально подробно " + new String(Character.toChars(unicode)));

        if (eventManager.getAction().equals("edit"))
            loadDescription();

        return view;
    }

    private void loadDescription() {
        editEventName.setText(eventManager.getDescription());
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
        if (Objects.requireNonNull(editEventName.getText()).toString().matches("")) {
            editEventName.setError("Поле является обязательным для заполнения");
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
