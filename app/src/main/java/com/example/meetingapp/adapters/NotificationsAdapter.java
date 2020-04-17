package com.example.meetingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetingapp.NotificationListener;
import com.example.meetingapp.R;
import com.example.meetingapp.models.Chat;
import com.example.meetingapp.models.EventRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

    private List<EventRequest> eventRequests;
    private Context mContext;
    private final String acceptMessage = "";
    private final String noAnswerMessage = "";

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    ValueEventListener requestListener;

    public NotificationsAdapter(Context mContext, List<EventRequest> eventRequests) {
        this.eventRequests = eventRequests;
        this.mContext = mContext;

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.notification_item, parent, false);
        return new NotificationsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final EventRequest eventRequest = eventRequests.get(position);
        if (eventRequest.getToUser().equals(firebaseUser.getUid()) && eventRequest.getDecision().equals("ACCEPT")){
            holder.decisionButtons.setVisibility(View.GONE);
            holder.username.setText("вы приняли этого кореша )0");
        }
        else if(eventRequest.getToUser().equals(firebaseUser.getUid()) && eventRequest.getDecision().equals("DECLINE")){
            holder.decisionButtons.setVisibility(View.GONE);
            holder.username.setText("ну и правильно, нахер он вам");
        }
        else if (eventRequest.getFromUser().equals(firebaseUser.getUid()) && eventRequest.getDecision().equals("ACCEPT")){
            holder.decisionButtons.setVisibility(View.GONE);
            holder.username.setText("тебя приняли, брат)");
        }
        else if(eventRequest.getFromUser().equals(firebaseUser.getUid()) && eventRequest.getDecision().equals("DECLINE")){
            holder.decisionButtons.setVisibility(View.GONE);
            holder.username.setText("ну и хер с ними, сам погуляешб 000000");
        }

        holder.acceptButton.setOnClickListener(v -> answerRequest(eventRequest.getFromUser(), "ACCEPT"));
        holder.declineButton.setOnClickListener(v -> answerRequest(eventRequest.getFromUser(), "DECLINE"));
    }

    private void answerRequest(String userId, String asnwer){
        databaseReference = FirebaseDatabase.getInstance().getReference("Request");
        requestListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    EventRequest eventRequest = snapshot.getValue(EventRequest.class);

                    if (eventRequest.getToUser().equals(firebaseUser.getUid()) && eventRequest.getFromUser().equals(userId)) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("decision", asnwer);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return eventRequests.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView username;
        private RelativeLayout decisionButtons;
        private Button acceptButton;
        private Button declineButton;

        ViewHolder(View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.notification_username);
            decisionButtons = itemView.findViewById(R.id.decision_buttons);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            declineButton = itemView.findViewById(R.id.declineButton);
        }
    }
}
