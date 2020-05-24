package com.example.meetingapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.meetingapp.DownloadImageTask;
import com.example.meetingapp.GetImageFromAsync;
import com.example.meetingapp.R;
import com.example.meetingapp.UserProfileManager;
import com.example.meetingapp.api.FirebaseClient;
import com.example.meetingapp.models.Message;
import com.example.meetingapp.models.UserProfile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;

    private List<Message> messages;

    private Context context;

    public MessageAdapter(Context context, List<Message> messages) {
        this.messages = messages;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.textMessage.setText(message.getText());
        holder.textUserName.setText(message.getFromUser().getFirstName());
        holder.textMessageTime.setText(parseCreated(message.getCreated()));

        if (message.getFromUser().getPhoto() != null) {
            holder.setImageProfile(message.getFromUser().getPhoto().getPhoto());
        } else {
            Glide.with(context).load(message.getFromUser().getPhoto()).into(holder.imageProfile);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        int meProfileId = UserProfileManager.getInstance().getMyProfile().getId();
        if (messages.get(position).getFromUser().getId() == meProfileId) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }

    private String parseCreated(String created) {
        ZonedDateTime zdt = null;
        String newFormat = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            zdt = ZonedDateTime.parse(created);
            newFormat = zdt.format(DateTimeFormatter.ofPattern("hh:mm"));
        }

        return newFormat;
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements GetImageFromAsync {

        @BindView(R.id.text_message)
        TextView textMessage;

        @BindView(R.id.text_message_time)
        TextView textMessageTime;

        @BindView(R.id.text_user_name)
        TextView textUserName;

        @BindView(R.id.image_profile)
        ImageView imageProfile;

        Bitmap bitmap;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setImageProfile(String photoUrl) {
            new DownloadImageTask(MessageAdapter.ViewHolder.this).execute(photoUrl);
        }

        @Override
        public void getResult(Bitmap bitmap) {
            imageProfile.setImageBitmap(bitmap);
            this.bitmap = bitmap;
        }
    }
}
