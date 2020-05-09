package com.example.meetingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.meetingapp.R;
import com.example.meetingapp.activities.EventActivity;
import com.example.meetingapp.models.Event;
import com.example.meetingapp.models.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ViewHolder> {
    private String theLastMessage;
    private Context context;
    private List<Event> events;
    private boolean is_chat;

    public ChatsAdapter(Context context, List<Event> events) {
        this.events = events;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Event event = events.get(position);

        holder.username.setText(event.getCreator().getEmail());
        if (event.getCreator().getPhoto() == null) {
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(context).load(event.getCreator().getPhoto()).into(holder.profile_image);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EventActivity.class);
            intent.putExtra("EXTRA_EVENT_ID", String.valueOf(event.getId()));

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    private void lastMessage(final String userid, final TextView last_msg) {
        last_msg.setText(theLastMessage);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView username;
        private ImageView profile_image;
        private ImageView img_on;
        private ImageView img_off;
        private TextView last_msg;

        ViewHolder(View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            profile_image = itemView.findViewById(R.id.profile_image);

            last_msg = itemView.findViewById(R.id.last_msg);

            img_on = itemView.findViewById(R.id.img_on);
            img_off = itemView.findViewById(R.id.img_off);
        }
    }
}
