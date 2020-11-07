package com.example.meetingapp.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.meetingapp.R;
import com.example.meetingapp.activities.MainActivity;
import com.example.meetingapp.adapters.ChatsAdapter;
import com.example.meetingapp.api.RetrofitClient;
import com.example.meetingapp.models.Chat;
import com.example.meetingapp.models.Event;
import com.example.meetingapp.models.Message;
import com.example.meetingapp.services.NetworkConnection;
import com.example.meetingapp.services.WebSocketListenerService;
import com.example.meetingapp.utils.PreferenceUtils;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChatsFragment extends Fragment {

    @BindView(R.id.recycle_view)
    RecyclerView recycleView;

    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private BroadcastReceiver broadcastReceiver;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);
        ButterKnife.bind(this, view);

        recycleView.setHasFixedSize(true);
        recycleView.setLayoutManager(new LinearLayoutManager(getContext()));

        swipeRefreshLayout.setOnRefreshListener(() -> {
            chats();
            swipeRefreshLayout.setRefreshing(false);
        });

        chats();

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int a = 5;
            }
        };

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        boolean isNetworkOnline = new NetworkConnection(getActivity()).isNetworkOnline(getActivity());
        if (!isNetworkOnline) {
            Toast.makeText(getActivity(), "Произошла сетевая ошибка. Проверьте что подключение к интернет работает стабильно.", Toast.LENGTH_SHORT).show();}

        LocalBroadcastManager.getInstance(requireContext()).registerReceiver((broadcastReceiver),
                new IntentFilter(WebSocketListenerService.EXTRA_RESULT)
        );
    }

    @Override
    public void onStop() {
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(broadcastReceiver);
        super.onStop();
    }

    @Override
    public void onResume() {
//        chats();
        super.onResume();
        
    }

    private void chats() {
        Call<List<Chat>> call = RetrofitClient
                .getInstance(PreferenceUtils.getToken(requireContext()))
                .getApi()
                .getEventChats();

        call.enqueue(new Callback<List<Chat>>() {
            @Override
            public void onResponse(@NonNull Call<List<Chat>> call, @NonNull Response<List<Chat>> response) {
                List<Chat> events = response.body();
                if (events != null) {
                    ChatsAdapter chatsAdapter = new ChatsAdapter(getContext(), events);
                    recycleView.setAdapter(chatsAdapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Chat>> call, @NonNull Throwable t) {

            }
        });
    }
}
