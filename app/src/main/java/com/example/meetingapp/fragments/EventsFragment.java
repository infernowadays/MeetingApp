package com.example.meetingapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetingapp.R;
import com.example.meetingapp.activities.MapsActivity;
import com.example.meetingapp.activities.PassEventBetweenStepsActivity;
import com.example.meetingapp.adapters.EventsAdapter;
import com.example.meetingapp.api.UserClient;
import com.example.meetingapp.models.Event;
import com.stepstone.stepper.StepperLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class EventsFragment extends Fragment {

    static final String BASE_URL = "http://10.0.2.2:8000/";

    private RecyclerView rvEvents;
    private Button geo_btn;
    private Button create_event_btn;
    private StepperLayout mStepperLayout;
    private FragmentActivity myContext;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_events, container, false);

        geo_btn = root.findViewById(R.id.geo_btn);
        geo_btn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MapsActivity.class);
            startActivity(intent);
        });

        create_event_btn = root.findViewById(R.id.new_event);
        create_event_btn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PassEventBetweenStepsActivity.class);
            startActivity(intent);
        });

        rvEvents = root.findViewById(R.id.my_recycler_view);
        rvEvents.setLayoutManager(new LinearLayoutManager(getContext()));
        events();

        return root;
    }

    private void events() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();
        UserClient userClient = retrofit.create(UserClient.class);

        Call<List<Event>> call = userClient.getEvents("Token asasddfgdhjkhlkjhkgjhf");

        call.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                List<Event> eventList = response.body();
                rvEvents.setAdapter(new EventsAdapter(getContext(), eventList));
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {

            }
        });
    }
}
