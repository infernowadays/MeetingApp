package com.example.meetingapp.ui.notifications;

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
import com.example.meetingapp.models.EventRequest;
import com.example.meetingapp.services.WebSocketListenerService;
import com.example.meetingapp.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;

public class NotificationsFragment extends Fragment {

    private static NotificationsFragment notificationsFragment;
    private static NotificationsAdapter notificationsAdapter;
    private static List<EventRequest> eventRequests;
    @BindView(R.id.recycle_view)
    RecyclerView recyclerView;

    private BroadcastReceiver broadcastReceiver;

    public static NotificationsFragment getInstance() {
        return notificationsFragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        ButterKnife.bind(this, view);

        notificationsFragment = this;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity().getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        eventRequests = new ArrayList<>();
        notificationsAdapter = new NotificationsAdapter(getContext(), eventRequests);

        loadNotifications();


        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                EventRequest eventRequest =  intent.getParcelableExtra(WebSocketListenerService.COPA_MESSAGE);
                eventRequests.add(eventRequest);
                notificationsAdapter.notifyItemInserted(0);
                recyclerView.scrollToPosition(0);
            }
        };



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver((broadcastReceiver),
                new IntentFilter(WebSocketListenerService.COPA_RESULT)
        );
    }

    @Override
    public void onStop() {
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(broadcastReceiver);
        super.onStop();
    }

    public void updateData(EventRequest eventRequest) {
        new Thread() {
            public void run() {

                try {
                    runOnUiThread(() -> {
                            eventRequests.add(eventRequest);
                            notificationsAdapter.notifyItemInserted(eventRequests.size() - 1);
                        });
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }.start();

//        Thread updaterThread = new Thread() {
//            public void run() {
//                try {
//                    runOnUiThread(() -> {
//                        eventRequests.add(eventRequest);
//                        notificationsAdapter.notifyItemInserted(eventRequests.size() - 1);
//                    });
//                } catch (Throwable throwable) {
//                    throwable.printStackTrace();
//                }
//            }
//        };
//        updaterThread.start();
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
                    recyclerView.setAdapter(notificationsAdapter);
//                    notificationsAdapter.notifyItemRangeInserted(0, eventRequests.size());

                }

            }

            @Override
            public void onFailure(@NonNull Call<List<EventRequest>> call, @NonNull Throwable t) {

            }
        });
    }
}