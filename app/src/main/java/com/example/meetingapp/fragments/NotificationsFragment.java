package com.example.meetingapp.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetingapp.R;
import com.example.meetingapp.adapters.NotificationsAdapter;
import com.example.meetingapp.api.RetrofitClient;
import com.example.meetingapp.models.Event;
import com.example.meetingapp.models.EventRequest;
import com.example.meetingapp.models.Message;
import com.example.meetingapp.services.WebSocketListenerService;
import com.example.meetingapp.utils.PreferenceUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsFragment extends Fragment {

    @BindView(R.id.recycle_view)
    RecyclerView recycleView;
    private NotificationsAdapter notificationsAdapter;
    private List<EventRequest> eventRequests;
    private BroadcastReceiver broadcastReceiver;
    private Gson gson;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        ButterKnife.bind(this, view);

        gson = new Gson();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity().getApplicationContext());
        recycleView.setLayoutManager(linearLayoutManager);

        eventRequests = new ArrayList<>();
        notificationsAdapter = new NotificationsAdapter(getContext(), eventRequests);

        loadNotifications();

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                EventRequest eventRequest = gson.fromJson(intent.getStringExtra(WebSocketListenerService.EXTRA_REQUEST), EventRequest.class);

                eventRequests.add(0, eventRequest);
                notificationsAdapter.notifyItemInserted(0);
                recycleView.smoothScrollToPosition(0);
            }
        };

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver((broadcastReceiver),
                new IntentFilter(WebSocketListenerService.EXTRA_RESULT)
        );
    }

    @Override
    public void onStop() {
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(broadcastReceiver);
        super.onStop();
    }

    private void loadNotifications() {
        Call<List<EventRequest>> call = RetrofitClient
                .getInstance(PreferenceUtils.getToken(requireContext()))
                .getApi()
                .getRequests();

        call.enqueue(new Callback<List<EventRequest>>() {
            @Override
            public void onResponse(@NonNull Call<List<EventRequest>> call, @NonNull Response<List<EventRequest>> response) {
                eventRequests = response.body();
                if (eventRequests != null) {
                    notificationsAdapter = new NotificationsAdapter(getContext(), eventRequests);
                    recycleView.setAdapter(notificationsAdapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<EventRequest>> call, @NonNull Throwable t) {

            }
        });
    }
}