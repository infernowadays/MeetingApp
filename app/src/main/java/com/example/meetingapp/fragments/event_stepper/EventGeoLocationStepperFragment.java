package com.example.meetingapp.fragments.event_stepper;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.meetingapp.R;
import com.example.meetingapp.activities.MapsActivity;
import com.example.meetingapp.models.MyLocation;
import com.google.android.material.button.MaterialButton;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;


public class EventGeoLocationStepperFragment extends Fragment implements Step {

    private MaterialEditText editAddress;
    private MyLocation myLocation;

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
