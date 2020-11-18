package com.example.meetingapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetingapp.R;
import com.example.meetingapp.adapters.EventsAdapter;
import com.example.meetingapp.api.RetrofitClient;
import com.example.meetingapp.models.Event;
import com.example.meetingapp.models.UserProfile;
import com.example.meetingapp.services.UserProfileManager;
import com.example.meetingapp.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

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
    private String me;
    private String requested;
    private String ended;
    private int lastEventId = 0;

    public HomeEventsFragment(String me, String requested, String ended) {
        this.me = me;
        this.requested = requested;
        this.ended = ended;
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
        UserProfile userProfile = UserProfileManager.getInstance().getMyProfile();
        recyclerView.setAdapter(eventsAdapter);
        events(me, requested, ended);

        return view;
    }

    private void events(String roles, String requested, String ended) {
        Call<List<Event>> call = RetrofitClient
                .getInstance(PreferenceUtils.getToken(requireContext()))
                .getApi()
                .getEvents(null, null, null, null, null, null, null, null, roles, requested, ended, 0, null);

        call.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(@NonNull Call<List<Event>> call, @NonNull Response<List<Event>> response) {
                events = response.body();
                eventsAdapter = new EventsAdapter(getContext(), events);
                recyclerView.setAdapter(eventsAdapter);
            }

            @Override
            public void onFailure(@NonNull Call<List<Event>> call, @NonNull Throwable t) {
                int a = 5;
            }
        });
    }
}
