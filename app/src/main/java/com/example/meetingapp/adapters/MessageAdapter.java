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
import com.example.meetingapp.models.UserProfile;
import com.example.meetingapp.models.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;
    private FirebaseUser firebaseUser;
    private Context context;
    private List<UserProfile> users;
    private List<Message> messages;
    private String imageURL;

    public MessageAdapter(Context mContext, List<Message> messages, List<UserProfile> users) {
        this.messages = messages;
        this.users = users;
        this.context = mContext;
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

        holder.show_message.setText(message.getMessage());
        for(UserProfile userProfile : users){
            if(message.getUid().equals(userProfile.getId())){
                if (userProfile.getImageURL().equals("default")) {
                    holder.profile_image.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(context).load(userProfile.getImageURL()).into(holder.profile_image);
                }
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
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (messages.get(position).getUid().equals(firebaseUser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView show_message;
        private ImageView profile_image;

        ViewHolder(View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.profile_image);
        }
    }
}
