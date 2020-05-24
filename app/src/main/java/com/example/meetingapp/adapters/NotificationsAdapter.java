package com.example.meetingapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetingapp.DownloadImageTask;
import com.example.meetingapp.GetImageFromAsync;
import com.example.meetingapp.R;
import com.example.meetingapp.UserProfileManager;
import com.example.meetingapp.api.RetrofitClient;
import com.example.meetingapp.models.EventRequest;
import com.example.meetingapp.models.UserProfile;
import com.example.meetingapp.utils.PreferenceUtils;

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

        holder.textFirstName.setText(eventRequest.getFromUser().getFirstName());
        if (eventRequest.getFromUser().getPhoto() != null) {
            holder.setImageProfile(eventRequest.getFromUser().getPhoto().getPhoto());
        }

        holder.textCreated.setText(parseCreated(eventRequest.getCreated()));

        if (eventRequest.getToUser().equals(String.valueOf(userProfile.getId())) && eventRequest.getDecision().equals("ACCEPT")) {
            holder.decisionButtons.setVisibility(View.GONE);
            holder.textFirstName.setText("Вы приняли " + eventRequest.getFromUser() + " в событие!");
        } else if (eventRequest.getToUser().equals(String.valueOf(userProfile.getId())) && eventRequest.getDecision().equals("DECLINE")) {
            holder.decisionButtons.setVisibility(View.GONE);
            holder.textFirstName.setText("Вы отклонили запрос");
        } else if (eventRequest.getFromUser().getId() == userProfile.getId() && eventRequest.getDecision().equals("ACCEPT")) {
            holder.decisionButtons.setVisibility(View.GONE);
            holder.textFirstName.setText("Добро пожаловать в событие!");
        } else if (eventRequest.getFromUser().getId() == userProfile.getId() && eventRequest.getDecision().equals("DECLINE")) {
            holder.decisionButtons.setVisibility(View.GONE);
            holder.textFirstName.setText("К сожалению, Ваша заявка была отклонена :(");
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

    private String parseCreated(String created) {
        String newFormat = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            ZonedDateTime zdt = ZonedDateTime.parse(created);
            newFormat = zdt.format(DateTimeFormatter.ofPattern("dd/MM hh:mm"));
        }

        SimpleDateFormat month_date = new SimpleDateFormat("dd MMMM в hh:mm", new Locale("RU"));
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM hh:mm");

        Date date = null;
        try {
            date = sdf.parse(Objects.requireNonNull(newFormat));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date != null) {
            return month_date.format(date);
        }

        return "";
    }

    @Override
    public int getItemCount() {
        return eventRequests.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements GetImageFromAsync {

        @BindView(R.id.image_profile)
        ImageView imageProfile;

        @BindView(R.id.notification_username)
        TextView textFirstName;

        @BindView(R.id.text_created)
        TextView textCreated;

        @BindView(R.id.decision_buttons)
        LinearLayout decisionButtons;

        @BindView(R.id.acceptButton)
        Button acceptButton;

        @BindView(R.id.declineButton)
        Button declineButton;

        Bitmap bitmap;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setImageProfile(String photoUrl) {
            new DownloadImageTask(NotificationsAdapter.ViewHolder.this).execute(photoUrl);
        }

        @Override
        public void getResult(Bitmap bitmap) {
            imageProfile.setImageBitmap(bitmap);
            this.bitmap = bitmap;
        }
    }
}
