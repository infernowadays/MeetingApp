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
import com.example.meetingapp.adapters.GroupChatsAdapter;
import com.example.meetingapp.models.ChatList;
import com.example.meetingapp.models.GroupChat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class GroupChatsFragment extends Fragment {

    private RecyclerView recyclerView;

    private GroupChatsAdapter groupChatsAdapter;
    private List<GroupChat> groupChats;

    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_group_chats, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        groupChats = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Chat");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                groupChats.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    GroupChat chat = snapshot.getValue(GroupChat.class);
                    if (chat != null && firebaseUser.getUid().equals(chat.getUser())) {
                        groupChats.add(chat);
                    }
                }

                if (groupChats == null) {
                    groupChatsAdapter = new GroupChatsAdapter(getContext(), groupChats);
                    recyclerView.setAdapter(groupChatsAdapter);
                } else {
                    groupChatsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }
}
