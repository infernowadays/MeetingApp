package com.example.meetingapp.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetingapp.R;
import com.example.meetingapp.UserProfileManager;
import com.example.meetingapp.adapters.EventsAdapter;
import com.example.meetingapp.api.RetrofitClient;
import com.example.meetingapp.models.Event;
import com.example.meetingapp.models.UserProfile;
import com.example.meetingapp.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeEventsFragment extends Fragment {

    private static HomeEventsFragment instance;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private List<Event> events;
    private EventsAdapter eventsAdapter;
    private String eventType = "";
    private UserProfile userProfile;

    public HomeEventsFragment(String eventType) {
        this.eventType = eventType;
    }

    public HomeEventsFragment() {

    }

    public static HomeEventsFragment getInstance() {
        return instance;
    }

    public void addCreatedEvent(Event event) {
        events.add(0, event);
        eventsAdapter.notifyItemInserted(0);
        recyclerView.smoothScrollToPosition(0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_events, container, false);
        ButterKnife.bind(this, view);

        instance = this;

        events = new ArrayList<>();
        userProfile = UserProfileManager.getInstance().getMyProfile();

        recyclerView.setAdapter(eventsAdapter);

        List<String> filters = new ArrayList<>();
        filters.add("creator");
        filters.add("member");

        events(filters);

        return view;
    }

    private void events(List<String> roles) {
        Call<List<Event>> call = RetrofitClient
                .getInstance(PreferenceUtils.getToken(requireContext()))
                .getApi()
                .getEvents(null, roles);

        call.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(@NonNull Call<List<Event>> call, @NonNull Response<List<Event>> response) {
                events = response.body();

                if (events == null || eventType.equals("passed"))
                    events = new ArrayList<>();

                if (eventType.equals("creator"))
                    events = eventsByCreator(events);

                if (eventType.equals("member"))
                    events = eventsByMember(events);

                eventsAdapter = new EventsAdapter(getContext(), events);
                recyclerView.setAdapter(eventsAdapter);
            }

            @Override
            public void onFailure(@NonNull Call<List<Event>> call, @NonNull Throwable t) {

            }
        });
    }

    private List<Event> eventsByCreator(List<Event> events) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (events != null) {
                events = events.stream().filter(event -> event.getCreator().getId() ==
                        userProfile.getId()).collect(Collectors.toList());
            }
        }

        return events;
    }

    private List<Event> eventsByMember(List<Event> events) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (events != null) {
                events = events.stream().filter(event ->
                        event.getMembers().stream().anyMatch(member -> member.getId() ==
                                (userProfile.getId()))).collect(Collectors.toList());
            }
        }

        return events;
    }
}
