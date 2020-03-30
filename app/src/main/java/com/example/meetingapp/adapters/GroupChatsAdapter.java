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

import com.example.meetingapp.R;
import com.example.meetingapp.activities.GroupMessageActivity;
import com.example.meetingapp.models.GroupChat;

import java.util.List;

public class GroupChatsAdapter extends RecyclerView.Adapter<GroupChatsAdapter.ViewHolder> {
    private String theLastMessage;
    private Context mContext;
    private List<GroupChat> groupChats;

    public GroupChatsAdapter(Context mContext, List<GroupChat> groupChats) {
        this.groupChats = groupChats;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public GroupChatsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item, parent, false);
        return new GroupChatsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final GroupChat groupChat = groupChats.get(position);

//        holder.eventDescription.setText(groupChat.getEventDescription());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, GroupMessageActivity.class);
            mContext.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return groupChats.size();
    }

    private void lastMessage(final String userid, final TextView last_msg) {

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

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
