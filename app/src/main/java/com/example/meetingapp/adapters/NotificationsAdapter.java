package com.example.meetingapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetingapp.R;
import com.example.meetingapp.UserProfileManager;
import com.example.meetingapp.api.RetrofitClient;
import com.example.meetingapp.models.EventRequest;
import com.example.meetingapp.models.UserProfile;
import com.example.meetingapp.utils.PreferenceUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

    private final String acceptMessage = "";
    private final String noAnswerMessage = "";
    private List<EventRequest> eventRequests;
    private Context mContext;
    private ViewHolder holder;
    private UserProfile userProfile;

    public NotificationsAdapter(Context mContext, List<EventRequest> eventRequests) {
        this.eventRequests = eventRequests;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.notification_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final EventRequest eventRequest = eventRequests.get(position);
        userProfile = UserProfileManager.getInstance().getMyProfile();

        holder.username.setText(eventRequest.getFromUser());

        if (eventRequest.getToUser().equals(String.valueOf(userProfile.getId())) && eventRequest.getDecision().equals("ACCEPT")) {
            holder.decisionButtons.setVisibility(View.GONE);
            holder.username.setText("Вы приняли " + eventRequest.getFromUser() + "в событие!");
        } else if (eventRequest.getToUser().equals(String.valueOf(userProfile.getId())) && eventRequest.getDecision().equals("DECLINE")) {
            holder.decisionButtons.setVisibility(View.GONE);
            holder.username.setText("Запрос отклонен.");
        } else if (eventRequest.getFromUser().equals(String.valueOf(userProfile.getId())) && eventRequest.getDecision().equals("ACCEPT")) {
            holder.decisionButtons.setVisibility(View.GONE);
            holder.username.setText("Добро пожаловать!");
        } else if (eventRequest.getFromUser().equals(String.valueOf(userProfile.getId())) && eventRequest.getDecision().equals("DECLINE")) {
            holder.decisionButtons.setVisibility(View.GONE);
            holder.username.setText("К сожалению, Ваша заявка была отклонена :(");
        } else {
            holder.decisionButtons.setVisibility(View.VISIBLE);
        }
        holder.acceptButton.setOnClickListener(v -> answerRequest(eventRequest, "ACCEPT"));
        holder.declineButton.setOnClickListener(v -> answerRequest(eventRequest, "DECLINE"));
    }

    private void answerRequest(EventRequest eventRequest, String decision) {
        Call<EventRequest> call = RetrofitClient
                .getInstance(PreferenceUtils.getToken(mContext))
                .getApi()
                .answerRequest(String.valueOf(eventRequest.getId()), new EventRequest(decision));

        call.enqueue(new Callback<EventRequest>() {
            @Override
            public void onResponse(@NonNull Call<EventRequest> call, @NonNull Response<EventRequest> response) {
                if (response.body() != null) {
                    eventRequest.setDecision(decision);
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<EventRequest> call, @NonNull Throwable t) {
                int a = 0;
            }
        });

    }

    @Override
    public int getItemCount() {
        return eventRequests.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.notification_username)
        TextView username;

        @BindView(R.id.decision_buttons)
        RelativeLayout decisionButtons;

        @BindView(R.id.acceptButton)
        Button acceptButton;

        @BindView(R.id.declineButton)
        Button declineButton;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
