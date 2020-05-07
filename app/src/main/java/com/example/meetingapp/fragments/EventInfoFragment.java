package com.example.meetingapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.meetingapp.R;
import com.example.meetingapp.api.FirebaseClient;
import com.example.meetingapp.api.RetrofitClient;
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

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventInfoFragment extends Fragment {

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

    private static Event event;
    private Context context;
    private GoogleMap googleMap;
    private static EventInfoFragment instance;

    public static EventInfoFragment getInstance(){
        return instance;
    }

    public static Event getEvent(){
        return event;
    }

    public static void setEvent(Event updatedEvent){
        event = updatedEvent;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        ButterKnife.bind(this, view);

        loadEvent();
        mapView.onCreate(savedInstanceState);

        instance = this;
        return view;
    }

    @OnClick(R.id.button_edit_event)
    void editEvent(){

    }

    private void initMapView() {
        mapView.onResume();

        try {
            MapsInitializer.initialize(requireActivity().getApplicationContext());
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
        String eventId = requireActivity().getIntent().getStringExtra("EXTRA_EVENT_ID");

        Call<Event> call = RetrofitClient
                .getInstance(PreferenceUtils.getToken(requireContext()))
                .getApi()
                .getEvent(eventId);

        call.enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                event = response.body();

                if (event != null) {
                    for (Category category : event.getCategories()) {
                        Chip chip = (Chip) getLayoutInflater().inflate(R.layout.category_item, chipGroup, false);
                        chip.setText(category.getName());
                        chipGroup.addView(chip);
                    }
                    putEvent();
                }
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {

            }
        });
    }

    private void putEvent() {
        textEventDescription.setText(String.valueOf(event.getDescription()));
        textEventLocation.setText(String.valueOf(event.getGeoPoint().getAddress()));
        textEventDate.setText(event.getDate());
        if(event.getTime() != null){
            layoutEventTime.setVisibility(View.VISIBLE);
            textEventTime.setText(event.getTime());
        }

        initMapView();
    }
}
