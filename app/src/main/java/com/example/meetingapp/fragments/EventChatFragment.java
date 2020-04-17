package com.example.meetingapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meetingapp.R;
import com.example.meetingapp.adapters.MessageAdapter;
import com.example.meetingapp.models.Chat;
import com.example.meetingapp.models.UserProfile;
import com.example.meetingapp.models.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class EventChatFragment extends Fragment {

    CircleImageView profile_image;
    TextView username;

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    Intent intent;

    ImageButton sendButton;
    EditText textSend;

    MessageAdapter messageAdapter;
    List<Chat> mchat;
    List<Message> messages;

    String user_id;

    RecyclerView recyclerView;
    ValueEventListener seenListener;

    private String eventId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_chat, container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        profile_image = view.findViewById(R.id.profile_image);
        username = view.findViewById(R.id.username);

        sendButton = view.findViewById(R.id.btn_send);
        textSend = view.findViewById(R.id.text_send);


        sendButton.setOnClickListener(v -> {
            String msg = textSend.getText().toString();
            if (!msg.equals("")) {
                sendMessage(firebaseUser.getUid(), Long.valueOf(eventId), msg, "12.01.2020", "15:43");
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "You can't send empty message", Toast.LENGTH_SHORT).show();
            }
            textSend.setText("");
        });

        eventId = getActivity().getIntent().getStringExtra("eventId");
        readMessages();

        return view;
    }

    private void sendMessage(String uid, Long event_id, String message, String date, String time) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", uid);
        hashMap.put("event_id", event_id);
        hashMap.put("message", message);
        hashMap.put("date", date);
        hashMap.put("time", time);

        reference.child("Message").push().setValue(hashMap);
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
                    if(String.valueOf(message.getEvent_id()).equals(eventId)){
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
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        List<UserProfile> users = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserProfile userProfile = snapshot.getValue(UserProfile.class);
                    for(Message message : messages){
                        if(message.getUid().equals(userProfile.getId())){
                            users.add(userProfile);
                        }
                    }
                }
                messageAdapter = new MessageAdapter(getContext(), messages, users);
                recyclerView.setAdapter(messageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
