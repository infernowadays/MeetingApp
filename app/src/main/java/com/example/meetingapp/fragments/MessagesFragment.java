package com.example.meetingapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetingapp.NotificationListener;
import com.example.meetingapp.R;
import com.example.meetingapp.adapters.NotificationsAdapter;
import com.example.meetingapp.models.EventRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MessagesFragment extends Fragment {

    private List<EventRequest> eventRequests;
    private NotificationsAdapter notificationsAdapter;
    private FirebaseUser firebaseUser;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        eventRequests = new ArrayList<>();


        recyclerView = view.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Objects.requireNonNull(getActivity()).getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Request");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eventRequests.clear();
                int notSeenNotifications = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    EventRequest eventRequest = snapshot.getValue(EventRequest.class);
                    if (eventRequest != null &&
                            eventRequest.getCreator_id().equals(firebaseUser.getUid())) {
                        eventRequests.add(eventRequest);

                        if (!eventRequest.isSeen()) {
                            notSeenNotifications++;
                        }
                    }
                    NotificationListener notificationListener = (NotificationListener) getActivity();
                    if (notificationListener != null) {
                        notificationListener.addNotificationBadge(notSeenNotifications);
                    }

                    notificationsAdapter = new NotificationsAdapter(getContext(), eventRequests);
                    recyclerView.setAdapter(notificationsAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }
}
