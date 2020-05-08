package com.example.meetingapp.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.meetingapp.R;
import com.example.meetingapp.UserProfileManager;
import com.example.meetingapp.activities.MapsActivity;
import com.example.meetingapp.activities.PassEventBetweenStepsActivity;
import com.example.meetingapp.adapters.EventsAdapter;
import com.example.meetingapp.api.RetrofitClient;
import com.example.meetingapp.models.Event;
import com.example.meetingapp.models.User;
import com.example.meetingapp.models.UserProfile;
import com.example.meetingapp.utils.PreferenceUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.stepstone.stepper.StepperLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EventsFragment extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.button_create_event)
    Button buttonCreateEvent;

    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private StepperLayout mStepperLayout;
    private FragmentActivity myContext;

    ProgressBar progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        ButterKnife.bind(this, view);

        progressBar = view.findViewById(R.id.progress_bar);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            events(null);
            swipeRefreshLayout.setRefreshing(false);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setAdapter(new EventsAdapter(getContext(), new ArrayList<>()));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        meProfile();
        events(null);

        return view;
    }

    @OnClick(R.id.button_open_map)
    void buttonOpenMap() {
        Intent intent = new Intent(getActivity(), MapsActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.button_create_event)
    void buttonCreateEvent() {
        Intent intent = new Intent(getActivity(), PassEventBetweenStepsActivity.class);
        intent.putExtra("action", "create");

        startActivity(intent);
    }

    @OnClick(R.id.button_filter_events)
    void buttonCreateFilterDialog(){
        EventsFilterDialog dialog = EventsFilterDialog.newInstance();
        dialog.setTargetFragment(EventsFragment.this, 1337);
        dialog.setCallback(this::events);
        dialog.show(requireActivity().getSupportFragmentManager(), "tag");
    }

    private void events(List<String> categories) {

        Call<List<Event>> call = RetrofitClient
                .getInstance(PreferenceUtils.getToken(requireContext()))
                .getApi()
                .getEvents(categories, null);

        call.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                List<Event> events = response.body();
                if(events != null)
                    recyclerView.setAdapter(new EventsAdapter(getContext(), events));




            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {

            }
        });
    }

    private void meProfile(){
        Call<UserProfile> call = RetrofitClient
                .getInstance(PreferenceUtils.getToken(requireContext()))
                .getApi()
                .meProfile();

        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                UserProfileManager.getInstance().initialize(response.body());
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {

            }
        });
    }
}