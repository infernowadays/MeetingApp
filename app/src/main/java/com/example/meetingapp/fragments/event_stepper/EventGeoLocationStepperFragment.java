package com.example.meetingapp.fragments.event_stepper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.meetingapp.R;
import com.example.meetingapp.activities.BottomNavigationActivity;
import com.example.meetingapp.activities.LoginActivity;
import com.example.meetingapp.activities.MapsActivity;
import com.example.meetingapp.models.Location;
import com.example.meetingapp.utils.PreferenceUtils;
import com.google.android.material.button.MaterialButton;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.Objects;


public class EventGeoLocationStepperFragment extends Fragment implements Step {

    private MaterialButton locationButton;

    public static EventGeoLocationStepperFragment newInstance() {
        return new EventGeoLocationStepperFragment();
    }

    public Fragment getInstance() {
        return getChildFragmentManager().findFragmentById(R.id.geo_fragment);
    }

    @Override
    public void onResume() {
        super.onResume();

        if( getArguments() != null) {
            String strtext = getArguments().getString("edttext");
        }


        Location location = PreferenceUtils.getLocation(Objects.requireNonNull(getActivity()));

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        String bundle = Objects.requireNonNull(data).getStringExtra("lol");
        int a = 5;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_geo_location_stepper, container, false);

        locationButton = view.findViewById(R.id.openGeo);
        locationButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MapsActivity.class);
//            intent.putExtra("my_context", (Parcelable) getActivity());
            startActivityForResult(intent, 1);
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
