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

public class UsersFragment extends Fragment {

    private RecyclerView recyclerView;

    private UsersAdapter usersAdapter;
    private List<ChatUser> chatUsers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_users, container, false);

        recyclerView = view.findViewById(R.id.recycle_view_users);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        chatUsers = new ArrayList<>();
        readUsers();

        return view;
    }

    private void readUsers() {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatUser chatUser = snapshot.getValue(ChatUser.class);

                    assert chatUser != null;
                    assert firebaseUser != null;
                    if (!chatUser.getId().equals(firebaseUser.getUid())) {
                        chatUsers.add(chatUser);
                    }

                    usersAdapter = new UsersAdapter(getContext(), chatUsers, false);
                    recyclerView.setAdapter(usersAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
