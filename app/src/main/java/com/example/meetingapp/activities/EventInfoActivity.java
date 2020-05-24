package com.example.meetingapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.meetingapp.R;
import com.example.meetingapp.api.RetrofitClient;
import com.example.meetingapp.fragments.EventInfoFragment;
import com.example.meetingapp.models.Category;
import com.example.meetingapp.models.Event;
import com.example.meetingapp.utils.PreferenceUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventInfoActivity extends AppCompatActivity {

    private static Event event;

    @BindView(R.id.text_event_creator)
    TextView textEventCreator;
    @BindView(R.id.text_event_description)
    TextView textEventDescription;
    @BindView(R.id.text_event_location)
    TextView textEventLocation;
    @BindView(R.id.text_event_date)
    TextView textEventDate;
    @BindView(R.id.text_event_time)
    TextView textEventTime;
    @BindView(R.id.event_time)
    LinearLayout layoutEventTime;
    @BindView(R.id.map_view)
    MapView mapView;
    @BindView(R.id.chip_group)
    ChipGroup chipGroup;
    @BindView(R.id.button_edit_event)
    ImageButton buttonEditEvent;
    private Context context;
    private GoogleMap googleMap;

    public static Event getEvent() {
        return event;
    }

    public static void setEvent(Event updatedEvent) {
        event = updatedEvent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);
        ButterKnife.bind(this);

        loadEvent();
        mapView.onCreate(savedInstanceState);
    }

    @OnClick(R.id.event_creator)
    void openCreatorProfile() {
        Intent intent = new Intent(this, UserProfileActivity.class);
        intent.putExtra("EXTRA_USER_PROFILE_ID", String.valueOf(event.getCreator().getId()));
        startActivity(intent);
    }

    @OnClick(R.id.button_edit_event)
    void editEvent() {

    }

    private void initMapView() {
        mapView.onResume();

        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView.getMapAsync(mMap -> {
            googleMap = mMap;
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            googleMap.getUiSettings().setCompassEnabled(false);
            googleMap.getUiSettings().setMapToolbarEnabled(false);

            LatLng latLng = new LatLng(event.getGeoPoint().getLatitude(), event.getGeoPoint().getLongitude());
            googleMap.addMarker(new MarkerOptions().position(latLng).title("Событие начнется здесь!"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadEvent();
    }

    private void loadEvent() {
        String eventId = getIntent().getStringExtra("EXTRA_EVENT_ID");

        Call<Event> call = RetrofitClient
                .getInstance(PreferenceUtils.getToken(this))
                .getApi()
                .getEvent(eventId);

        call.enqueue(new Callback<Event>() {
            @Override
            public void onResponse(@NonNull Call<Event> call, @NonNull Response<Event> response) {
                event = response.body();

                if (event != null) {
                    chipGroup.removeAllViews();
                    for (Category category : event.getCategories()) {
                        Chip chip = (Chip) getLayoutInflater().inflate(R.layout.category_item, chipGroup, false);
                        chip.setText(category.getName());
                        chipGroup.addView(chip);
                    }
                    putEvent();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Event> call, @NonNull Throwable t) {

            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void putEvent() {
        textEventCreator.setText(event.getCreator().getFirstName() + " " +
                event.getCreator().getLastName());
        textEventDescription.setText(String.valueOf(event.getDescription()));
        textEventLocation.setText(String.valueOf(event.getGeoPoint().getAddress()));
        textEventDate.setText(event.getDate());
        if (event.getTime() != null) {
            layoutEventTime.setVisibility(View.VISIBLE);
            textEventTime.setText(event.getTime());
        }

        initMapView();
    }
}
