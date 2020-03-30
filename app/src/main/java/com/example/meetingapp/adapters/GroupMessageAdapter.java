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
import com.example.meetingapp.activities.MessageActivity;
import com.example.meetingapp.models.ChatUser;
import com.example.meetingapp.models.GroupChat;

import java.util.List;

public class GroupMessageAdapter extends RecyclerView.Adapter<GroupChatsAdapter.ViewHolder> {

    private String theLastMessage;
    private Context mContext;
    private List<GroupChat> groupChats;

    public GroupMessageAdapter(Context mContext, List<GroupChat> groupChats) {
        this.groupChats = groupChats;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public GroupChatsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
        return new GroupChatsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupChatsAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return groupChats.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView eventDescription;
        private ImageView profileImage;
        private TextView lastMessage;

        ViewHolder(View itemView) {
            super(itemView);

            eventDescription = itemView.findViewById(R.id.eventDescription);
            profileImage = itemView.findViewById(R.id.profileImage);
            lastMessage = itemView.findViewById(R.id.lastMessage);
        }
    }
}
