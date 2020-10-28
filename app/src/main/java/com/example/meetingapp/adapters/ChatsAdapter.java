package com.example.meetingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.meetingapp.R;
import com.example.meetingapp.activities.EventActivity;
import com.example.meetingapp.activities.TicketActivity;
import com.example.meetingapp.interfaces.GetImageFromAsync;
import com.example.meetingapp.models.Chat;
import com.example.meetingapp.utils.images.DownloadImageTask;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ViewHolder> {
    private String theLastMessage;
    private Context context;
    private List<Chat> events;
    private boolean is_chat;

    public ChatsAdapter(Context context, List<Chat> events) {
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

        final Chat event = events.get(position);

        holder.textFirstName.setText(event.getFromUser().getFirstName());
        holder.textDescription.setText(event.getTitle());

        if (event.getFromUser().getPhoto() != null) {
            holder.setImageProfile(event.getFromUser().getPhoto().getPhoto());
        } else {
            Glide.with(context).load(event.getFromUser().getPhoto()).into(holder.imageProfile);
        }

        holder.itemView.setOnClickListener(v -> {
            if (event.getContentType().equals("message")) {
                Intent intent = new Intent(context, EventActivity.class);
                intent.putExtra("EXTRA_EVENT_ID", String.valueOf(event.getContentId()));
                context.startActivity(intent);
            }

            if (event.getContentType().equals("private_message")) {
                Intent intent = new Intent(context, TicketActivity.class);
                intent.putExtra("EXTRA_TICKET_ID", String.valueOf(event.getContentId()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements GetImageFromAsync {

        @BindView(R.id.text_first_name)
        TextView textFirstName;

        @BindView(R.id.text_event_description_brief)
        TextView textDescription;

        @BindView(R.id.image_profile)
        CircleImageView imageProfile;

        Bitmap bitmap;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setImageProfile(String photoUrl) {
            new DownloadImageTask(ChatsAdapter.ViewHolder.this).execute(photoUrl);
        }

        @Override
        public void getResult(Bitmap bitmap) {
            if (bitmap != null) {
                imageProfile.setImageBitmap(bitmap);
                this.bitmap = bitmap;
            }
        }
    }
}
