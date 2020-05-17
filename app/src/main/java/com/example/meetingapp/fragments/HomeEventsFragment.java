package com.example.meetingapp.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetingapp.R;
import com.example.meetingapp.adapters.EventsAdapter;
import com.example.meetingapp.api.FirebaseClient;
import com.example.meetingapp.api.RetrofitClient;
import com.example.meetingapp.models.Event;
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
    public String type1;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    int mScrollPosition = 0;
    LinearLayoutManager linearLayoutManager;
    View view;

    public HomeEventsFragment(String eventType) {
        this.type1 = eventType;
    }

    public HomeEventsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_events, container, false);
        ButterKnife.bind(this, view);

        instance = this;

        linearLayoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new EventsAdapter(getContext(), new ArrayList<>()));

//        recyclerView.setNestedScrollingEnabled(false);
//        recyclerView.setHasFixedSize(true);

        List<String> lll = new ArrayList<>();
        lll.add("creator");
        lll.add("member");

        events(lll);

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
                List<Event> events = response.body();

                if (events == null)
                    events = new ArrayList<>();


                if (type1.equals("creator"))
                    events = eventsByCreator(events);

                if (type1.equals("member"))
                    events = eventsByMember(events);

                if (type1.equals("passed"))
                    events = new ArrayList<>();


                recyclerView.setAdapter(new EventsAdapter(getContext(), events));
            }

            @Override
            public void onFailure(@NonNull Call<List<Event>> call, @NonNull Throwable t) {

            }
        });
    }

    private List<Event> eventsByCreator(List<Event> events) {
        FirebaseClient firebaseClient = new FirebaseClient(getContext());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

//            if (events != null) {
//                events = events.stream().filter(event ->
//                        event.getCreator().getFirebaseUid().equals(firebaseClient.getUid())).collect(Collectors.toList());
//            }
        }

        return events;
    }

    private List<Event> eventsByMember(List<Event> events) {
        FirebaseClient firebaseClient = new FirebaseClient(getContext());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            if (events != null) {
                events = events.stream().filter(event ->
                        event.getMembers().stream().anyMatch(member -> member.getFirebaseUid().equals(firebaseClient.getUid()))).collect(Collectors.toList());
            }
        }

        return events;
    }
}
