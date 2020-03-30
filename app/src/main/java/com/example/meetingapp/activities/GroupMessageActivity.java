package com.example.meetingapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.meetingapp.R;
import com.example.meetingapp.adapters.GroupMessageAdapter;
import com.example.meetingapp.adapters.MessageAdapter;
import com.example.meetingapp.models.Chat;
import com.example.meetingapp.models.ChatUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupMessageActivity extends AppCompatActivity {

//    CircleImageView profile_image;
//    TextView username;
//
//    FirebaseUser firebaseUser;
//    DatabaseReference databaseReference;
//
//    Intent intent;
//
//    ImageButton sendButton;
//    EditText textSend;
//
//    MessageAdapter messageAdapter;
//    List<Chat> mchat;
//
//    String user_id;
//
//    RecyclerView recyclerView;
//    ValueEventListener seenListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_message);

//        recyclerView = findViewById(R.id.recycler_view);
//        recyclerView.setHasFixedSize(true);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
//        linearLayoutManager.setStackFromEnd(true);
//        recyclerView.setLayoutManager(linearLayoutManager);
//
//        profile_image = findViewById(R.id.profile_image);
//        username = findViewById(R.id.username);
//
//        sendButton = findViewById(R.id.btn_send);
//        textSend = findViewById(R.id.text_send);
//
//        intent = getIntent();
//        user_id = intent.getStringExtra("userId");
//
//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//
//        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                ChatUser chatUser = dataSnapshot.getValue(ChatUser.class);
//
//                if (chatUser != null) {
//                    username.setText(chatUser.getUsername());
//                    if (chatUser.getImageURL().equals("default")) {
//                        profile_image.setImageResource(R.mipmap.ic_launcher);
//                    } else {
//                        Glide.with(getApplicationContext()).load(chatUser.getImageURL()).into(profile_image);
//                    }
//                    readMessages(firebaseUser.getUid(), user_id, chatUser.getImageURL());
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

    private void readMessages(final String myid, final String userid, final String imageurl) {
//        mchat = new ArrayList<>();
//
//        databaseReference = FirebaseDatabase.getInstance().getReference("Message");
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                mchat.clear();
//
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Message message = snapshot.getValue(Message.class);
//
//                    if (message.getEvent().equals(eventId)) {
//                        messages.add(message);
//                    }
//
//                    messageAdapter = new GroupMessageAdapter(GroupMessageActivity.this, mchat, imageurl);
//                    recyclerView.setAdapter(messageAdapter);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }
}
