package com.example.meetingapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.meetingapp.R;
import com.example.meetingapp.adapters.ChatsAdapter;
import com.example.meetingapp.api.RetrofitClient;
import com.example.meetingapp.models.Event;
import com.example.meetingapp.utils.PreferenceUtils;

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

        return view;
    }

    @Override
    public void onResume() {
        chats();
        super.onResume();
    }

    private void chats() {
        Call<List<Event>> call = RetrofitClient
                .getInstance(PreferenceUtils.getToken(requireContext()))
                .getApi()
                .getEventChats();

        call.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(@NonNull Call<List<Event>> call, @NonNull Response<List<Event>> response) {
                List<Event> events = response.body();
                if (events != null) {
                    ChatsAdapter chatsAdapter = new ChatsAdapter(getContext(), events);
                    recycleView.setAdapter(chatsAdapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Event>> call, @NonNull Throwable t) {

            }
        });
    }
}
