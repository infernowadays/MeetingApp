package com.example.meetingapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetingapp.R;
import com.example.meetingapp.adapters.MessageAdapter;
import com.example.meetingapp.api.FirebaseClient;
import com.example.meetingapp.models.Message;
import com.example.meetingapp.models.UserProfile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class EventChatFragment extends Fragment {

    @BindView(R.id.button_send)
    ImageButton buttonSend;

    @BindView(R.id.text_message)
    EditText textMessage;

    @BindView(R.id.recycle_view)
    RecyclerView recycleView;

    private MessageAdapter messageAdapter;
    private List<Message> messages;

    private FirebaseClient firebaseClient;
    private DatabaseReference databaseReference;
    private String eventId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_chat, container, false);
        ButterKnife.bind(this, view);

        recycleView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity().getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recycleView.setLayoutManager(linearLayoutManager);

        firebaseClient = new FirebaseClient(getContext());

        eventId = requireActivity().getIntent().getStringExtra("EXTRA_EVENT_ID");
        readMessages();

        return view;
    }

    @OnClick(R.id.button_send)
    void buttonSend() {
        String message = textMessage.getText().toString();
        if (!message.equals("")) {
            String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
            firebaseClient.sendMessage(Long.valueOf(eventId), message, currentDate, currentTime);
        } else {
            Toast.makeText(requireActivity().getApplicationContext(), "You can't send empty message", Toast.LENGTH_SHORT).show();
        }
        textMessage.setText("");
    }

    private void readMessages() {
        messages = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Message");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messages.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message message = snapshot.getValue(Message.class);
                    if (message != null && String.valueOf(message.getEventId()).equals(eventId)) {
                        messages.add(message);
                    }

                    getChatUsers(messages);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getChatUsers(List<Message> messages) {
        List<UserProfile> users = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserProfile userProfile = snapshot.getValue(UserProfile.class);
                    for (Message message : messages) {
//                        if (userProfile != null && message.getFirebaseUid().equals(userProfile.getFirebaseUid())) {
//                            users.add(userProfile);
//                        }
                    }
                }
                messageAdapter = new MessageAdapter(getContext(), messages, users);
                recycleView.setAdapter(messageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
