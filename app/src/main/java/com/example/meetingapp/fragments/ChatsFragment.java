package com.example.meetingapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetingapp.R;
import com.example.meetingapp.adapters.UsersAdapter;
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


public class ChatsFragment extends Fragment {

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private UsersAdapter usersAdapter;
    private List<ChatUser> mUsers;
    private List<String> usersList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        usersList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    assert chat != null;

                    if (chat.getSender().equals(firebaseUser.getUid())) {
                        usersList.add(chat.getReceiver());
                    }
                    if (chat.getReceiver().equals(firebaseUser.getUid())) {
                        usersList.add(chat.getSender());
                    }
                }

                readChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }

    private void readChats() {
        mUsers = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatUser chatUser = snapshot.getValue(ChatUser.class);

                    for (String id : usersList) {
                        if (chatUser.getId().equals(id)) {
                            if (mUsers.size() != 0) {
                                boolean counter = false;
                                for (ChatUser chatUser1 : mUsers) {
                                    if (chatUser.getId().equals(chatUser1.getId())) {
                                        counter = true;
                                    }
                                }
                                if(!counter)
                                    mUsers.add(chatUser);
                            } else {
                                mUsers.add(chatUser);
                            }
                        }
                    }
                }

                usersAdapter = new UsersAdapter(getContext(), mUsers, true);
                recyclerView.setAdapter(usersAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
