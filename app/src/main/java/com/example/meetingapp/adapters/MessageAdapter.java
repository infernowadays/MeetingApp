package com.example.meetingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.meetingapp.R;
import com.example.meetingapp.api.FirebaseClient;
import com.example.meetingapp.models.Message;
import com.example.meetingapp.models.UserProfile;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;

    private List<UserProfile> users;
    private List<Message> messages;

    private Context context;
    private FirebaseClient firebaseClient;

    public MessageAdapter(Context context, List<Message> messages, List<UserProfile> users) {
        this.messages = messages;
        this.users = users;
        this.context = context;
        firebaseClient = new FirebaseClient(context);
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.textMessage.setText(message.getMessage());

        for (UserProfile userProfile : users) {
            if (message.getFirebaseUid().equals(userProfile.getFirebaseUid())) {
//                if (userProfile.getImageUrl().equals("default")) {
//                    holder.imageProfile.setImageResource(R.mipmap.ic_launcher);
//                } else {
//                    Glide.with(context).load(userProfile.getImageUrl()).into(holder.imageProfile);
//                }
            }
        }


//        if(position == chats.size() - 1){
//            if(chat.isSeen()){
//                holder.txt_seen.setText("Просмотрено");
//            } else{
//              holder.txt_seen.setText("Доставлено");
//            }
//        } else{
//            holder.txt_seen.setVisibility(View.GONE);
//        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).getFirebaseUid().equals(firebaseClient.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_message)
        TextView textMessage;

        @BindView(R.id.image_profile)
        ImageView imageProfile;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
