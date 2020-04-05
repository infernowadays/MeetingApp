package com.example.meetingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetingapp.R;
import com.example.meetingapp.models.Notification;

import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

    private List<Notification> notifications;
    private Context mContext;
    private final String acceptMessage = "";
    private final String noAnswerMessage = "";

    public NotificationsAdapter(Context mContext, List<Notification> notifications) {
        this.notifications = notifications;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.notification_item, parent, false);
        return new NotificationsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Notification notification = notifications.get(position);
        if (notification.getDecision().equals("ACCEPT")){
            holder.decisionButtons.setVisibility(View.GONE);
            holder.username.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView username;
        private RelativeLayout decisionButtons;

        ViewHolder(View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.notification_username);
            decisionButtons = itemView.findViewById(R.id.decision_buttons);
        }
    }
}
