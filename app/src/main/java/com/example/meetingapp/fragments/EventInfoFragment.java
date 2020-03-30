package com.example.meetingapp.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.meetingapp.R;
import com.example.meetingapp.activities.MapsActivity;
import com.example.meetingapp.api.UserClient;
import com.example.meetingapp.models.Category;
import com.example.meetingapp.models.Event;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class EventInfoFragment extends Fragment {

    private TextView eventDescription;
    private TextView eventLocation;
    private TextView eventDate;
    private TextView eventTime;

    private ChipGroup chipGroup;
    private Event event;
    private Context context;
    private RecyclerView recyclerView;
    static final String BASE_URL = "http://10.0.2.2:8000/";

    private static final int REQUEST_CODE = 101;
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleMap map;


    private MapView mMapView;
    private GoogleMap googleMap;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);

        loadEvent();

        mMapView = view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        eventDescription = view.findViewById(R.id.eventDescription);
        eventLocation = view.findViewById(R.id.eventLocation);
        eventDate = view.findViewById(R.id.eventDate);
        eventTime = view.findViewById(R.id.eventTime);

        chipGroup = view.findViewById(R.id.chipGroup);



        return view;
    }

    private void initMapView() {
        mMapView.onResume();

        try {
            MapsInitializer.initialize(Objects.requireNonNull(getActivity()).getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(mMap -> {
            googleMap = mMap;
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            googleMap.getUiSettings().setCompassEnabled(false);
            googleMap.getUiSettings().setMapToolbarEnabled(false);

            LatLng latLng = new LatLng(event.getGeoPoint().getLatitude(), event.getGeoPoint().getLongitude());
            googleMap.addMarker(new MarkerOptions().position(latLng).title("Событие начнется здесь!"));

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
        });
    }

    private void loadEvent() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();
        UserClient userClient = retrofit.create(UserClient.class);

        String pk = Objects.requireNonNull(getActivity()).getIntent().getStringExtra("eventId");
        Call<Event> call = userClient.getEvent(pk, "Token 9ba875f0b1b909484e327292bd5d01be30c75791");

        call.enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                event = response.body();

                assert event != null;
                for(Category category : event.getCategories()){
                    Chip chip = (Chip) getLayoutInflater().inflate(R.layout.category_item, chipGroup, false);
                    chip.setText(category.getName());
                    chipGroup.addView(chip);
                }
                putEvent();
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {

            }
        });
    }

    private void putEvent(){
        eventDescription.setText(String.valueOf(event.getDescription()));
        eventLocation.setText(String.valueOf(event.getGeoPoint().getAddress()));
        eventDate.setText(event.getDate());
        eventTime.setText(event.getTime());
        initMapView();
    }
}
