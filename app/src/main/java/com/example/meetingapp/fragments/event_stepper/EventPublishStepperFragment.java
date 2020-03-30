package com.example.meetingapp.fragments.event_stepper;

import android.content.Context;
import android.content.Intent;
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
import com.example.meetingapp.activities.EventActivity;
import com.example.meetingapp.api.UserClient;
import com.example.meetingapp.models.Event;
import com.google.android.material.textview.MaterialTextView;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class EventPublishStepperFragment extends Fragment implements BlockingStep {

    private static final String BASE_URL = "http://10.0.2.2:8000/";

    private EventManager eventManager;
    private MaterialTextView description;
    private MaterialTextView date;
    private MaterialTextView time;
    private MaterialTextView address;
    private Event event;
    private Event createdEvent;

    public static EventPublishStepperFragment newInstance() {
        return new EventPublishStepperFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_publish_stepper, container, false);
        event = new Event();
        createdEvent = null;

        description = view.findViewById(R.id.create_event_description);
        date = view.findViewById(R.id._create_event_date);
        time = view.findViewById(R.id.create_event_time);
        address = view.findViewById(R.id.create_event_address);

        return view;
    }

    private void publishEvent() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();
        UserClient userClient = retrofit.create(UserClient.class);

        Call<Event> call = userClient.createEvent(event, "Token 9ba875f0b1b909484e327292bd5d01be30c75791");
        call.enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                createdEvent = response.body();
                if(createdEvent != null){
                    Toast.makeText(getActivity(), "Событие создано!", Toast.LENGTH_SHORT).show();
                    openCreatedEvent();
                }
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                Toast.makeText(getActivity(), "Что-то случилось :(", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openCreatedEvent() {
        Intent intent = new Intent(getActivity(), EventActivity.class);
        intent.putExtra("eventId", String.valueOf(createdEvent.getId()));

        Objects.requireNonNull(getActivity()).finish();
        startActivity(intent);
    }

    @Override
    public void onAttach(@androidx.annotation.NonNull Context context) {
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
        return null;
    }

    @Override
    public void onSelected() {
        event.setDescription(eventManager.getDescription());
        description.setText(eventManager.getDescription());

        event.setGeoPoint(eventManager.getLocation());
        address.setText(eventManager.getLocation().getAddress());

        event.setDate(eventManager.getDate());
        date.setText(eventManager.getDate());


        if (!eventManager.getTime().equals("")) {
            time.setText(eventManager.getTime());
            event.setTime(eventManager.getTime());
        }
    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {

    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {
        publishEvent();
    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();
    }
}
