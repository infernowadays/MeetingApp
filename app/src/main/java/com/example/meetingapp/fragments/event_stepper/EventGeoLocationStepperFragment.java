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
import com.example.meetingapp.activities.MapsActivity;
import com.example.meetingapp.api.UserClient;
import com.example.meetingapp.models.Event;
import com.example.meetingapp.models.MyLocation;
import com.example.meetingapp.models.Test;
import com.google.android.material.button.MaterialButton;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class EventGeoLocationStepperFragment extends Fragment implements BlockingStep {

    private static final String BASE_URL = "http://10.0.2.2:8000/";
    private MaterialEditText editAddress;
    private MyLocation myLocation;
    private EventManager eventManager;

    public static EventGeoLocationStepperFragment newInstance() {
        return new EventGeoLocationStepperFragment();
    }

    public Fragment getInstance() {
        return getChildFragmentManager().findFragmentById(R.id.geo_fragment);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data != null && data.hasExtra("newLocation")) {
            myLocation = data.getParcelableExtra("newLocation");
            String address = null;
            if (myLocation != null) {
                address = myLocation.getAddress();
                editAddress.setText(address);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_geo_location_stepper, container, false);

        createEvent();

        editAddress = view.findViewById(R.id.addressEdit);
        myLocation = null;

        MaterialButton locationButton = view.findViewById(R.id.openGeo);
        locationButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MapsActivity.class);
            intent.putExtra("prevLocation", myLocation);
            startActivityForResult(intent, 1);
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

    private void createEvent() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();
        UserClient userClient = retrofit.create(UserClient.class);

        Call<Event> call = userClient.createEvent(new Test("suuuuuuuuuu"), "Token 9ba875f0b1b909484e327292bd5d01be30c75791");
        call.enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                Event createdEvent = response.body();
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {

            }
        });
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        if (myLocation == null) {
            Toast.makeText(getActivity(), "Введите адрес )", Toast.LENGTH_SHORT).show();

            return new VerificationError("empty address");
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
//        String address = Objects.requireNonNull(editAddress.getText()).toString();
        eventManager.saveLocation(myLocation);
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
