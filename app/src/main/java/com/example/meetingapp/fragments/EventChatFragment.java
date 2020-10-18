package com.example.meetingapp.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetingapp.R;
import com.example.meetingapp.adapters.MessageAdapter;
import com.example.meetingapp.api.RetrofitClient;
import com.example.meetingapp.models.Event;
import com.example.meetingapp.models.Message;
import com.example.meetingapp.services.WebSocketListenerService;
import com.example.meetingapp.utils.PreferenceUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;

import java.security.Policy;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EventChatFragment extends Fragment {

    @BindView(R.id.button_send)
    ImageButton buttonSend;

    @BindView(R.id.text_message)
    EditText textMessage;

    @BindView(R.id.recycle_view)
    RecyclerView recycleView;

    @BindView(R.id.scroll_down_btn)
    ImageView scroll_down_btn;

    private MessageAdapter messageAdapter;
    private List<Message> messages;

    private DatabaseReference databaseReference;
    private String eventId;
    private BroadcastReceiver broadcastReceiver;

    private Event event;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_chat, container, false);
        ButterKnife.bind(this, view);

        recycleView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity().getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recycleView.setLayoutManager(linearLayoutManager);

        recycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager=LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
                assert layoutManager != null;
                int totalItemCount = layoutManager.getItemCount();
                int lastVisible = layoutManager.findLastVisibleItemPosition();

                boolean endHasBeenReached = lastVisible + 10 >= totalItemCount;
                if (totalItemCount > 0 && endHasBeenReached) {
                    scroll_down_btn.setVisibility(View.GONE);
                } else {
                    scroll_down_btn.setVisibility(View.VISIBLE);
                }
            }
        });

        loadEvent();

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.hasExtra(WebSocketListenerService.EXTRA_MESSAGE)) {
                    Gson gson = new Gson();
                    Message message = gson.fromJson(intent.getStringExtra(
                            WebSocketListenerService.EXTRA_MESSAGE), Message.class);

                    messages.add(message);
                    messageAdapter.notifyItemInserted(messages.size() - 1);
                    recycleView.smoothScrollToPosition(messages.size() - 1);
                }
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

    @OnClick(R.id.button_send)
    void buttonSend() {
        String message = textMessage.getText().toString();
        if (!message.equals("")) {
            String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
            sendMessage(message, currentDate);
        } else {
            Toast.makeText(requireActivity().getApplicationContext(), "You can't send empty message", Toast.LENGTH_SHORT).show();
        }
        textMessage.setText("");
    }

    @OnClick(R.id.scroll_down_btn)
    void scrollDown(){
        recycleView.scrollToPosition(messages.size()-1);
    }

    private void sendMessage(String text, String date) {
        Message message = new Message(text, date, false, event.getId());

        Call<Message> call = RetrofitClient
                .getInstance(PreferenceUtils.getToken(requireContext()))
                .getApi()
                .sendMessage(message);

        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(@NonNull Call<Message> call, @NonNull Response<Message> response) {
                Message message = response.body();
                if (message != null) {
                    messages.add(message);
                    recycleView.scrollToPosition(messages.size() - 1);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Message> call, @NonNull Throwable t) {

            }
        });
    }

    private void readMessages() {
        messages = new ArrayList<>();

        Call<List<Message>> call = RetrofitClient
                .getInstance(PreferenceUtils.getToken(requireContext()))
                .getApi()
                .getEventMessages(String.valueOf(event.getId()));

        call.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(@NonNull Call<List<Message>> call, @NonNull Response<List<Message>> response) {
                messages = response.body();
                if (messages != null) {
                    messageAdapter = new MessageAdapter(getContext(), messages);
                    recycleView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Message>> call, @NonNull Throwable t) {
                int a = 5;
            }
        });
    }

    private void loadEvent() {
        String eventId = requireActivity().getIntent().getStringExtra("EXTRA_EVENT_ID");

        Call<Event> call = RetrofitClient
                .getInstance(PreferenceUtils.getToken(requireContext()))
                .getApi()
                .getEvent(eventId);

        call.enqueue(new Callback<Event>() {
            @Override
            public void onResponse(@NonNull Call<Event> call, @NonNull Response<Event> response) {
                event = response.body();
                readMessages();

            }

            @Override
            public void onFailure(@NonNull Call<Event> call, @NonNull Throwable t) {

            }
        });
    }
}
