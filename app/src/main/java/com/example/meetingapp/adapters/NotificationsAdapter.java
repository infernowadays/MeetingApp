package com.example.meetingapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetingapp.DownloadImageTask;
import com.example.meetingapp.GetImageFromAsync;
import com.example.meetingapp.R;
import com.example.meetingapp.UserProfileManager;
import com.example.meetingapp.api.RetrofitClient;
import com.example.meetingapp.models.RequestGet;
import com.example.meetingapp.models.RequestSend;
import com.example.meetingapp.models.UserProfile;
import com.example.meetingapp.utils.PreferenceUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

    private final String WANT_JOIN = " хочет вступить в ваше событие";
    private final String SEND_REQUEST = "\nЗаявка отправлена"; // remove
    private final String ACCEPT = " теперь участвует в вашем событии!";
    private final String ACCEPTED = " принял вашу заявку в событие";
    private final String DECLINE = "\nЗаявка отклонена"; // remove
    private final String DECLINED = " отклонил вашу заявку";

    private List<RequestGet> eventRequests;
    private Context mContext;
    private ViewHolder holder;

    public NotificationsAdapter(Context mContext, List<RequestGet> eventRequests) {
        removeNoAnswerRequests(eventRequests);

        this.mContext = mContext;
    }

    private void removeNoAnswerRequests(List<RequestGet> eventRequests) {


        for (Iterator<RequestGet> iterator = eventRequests.iterator(); iterator.hasNext(); ) {
            RequestGet eventRequest = iterator.next();
            if ((eventRequest.getFromUser().getId() == UserProfileManager.getInstance().getMyProfile().getId() &&
                    eventRequest.getDecision().equals("NO_ANSWER")) ||
                    eventRequest.getToUser().getId() == UserProfileManager.getInstance().getMyProfile().getId() && eventRequest.getDecision().equals("DECLINE")) {

                iterator.remove();
            }
        }

        this.eventRequests = eventRequests;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.notification_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final RequestGet eventRequest = eventRequests.get(position);
        UserProfile userProfile = UserProfileManager.getInstance().getMyProfile();

        holder.textUserName.setText(eventRequest.getFromUser().getFirstName());

        ClickableSpan linkClick = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Link Click", Toast.LENGTH_SHORT).show();
                view.invalidate();
            }

            @SuppressLint("ResourceAsColor")
            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                ds.setTypeface(Typeface.create("Arial", Typeface.BOLD));

                if (holder.textUserName.isPressed()) {
                    ds.bgColor = ContextCompat.getColor(mContext, R.color.backgroundPrimaryLight);
                }
                holder.textUserName.invalidate();
            }
        };

        String fromUser = eventRequest.getFromUser().getFirstName() + " " + eventRequest.getFromUser().getLastName();
        String toUser = eventRequest.getToUser().getFirstName() + " " + eventRequest.getToUser().getLastName();
        holder.textUserName.setHighlightColor(Color.TRANSPARENT);

        if (eventRequest.getToUser().getId() == userProfile.getId() && eventRequest.getToUser().getPhoto() != null) {
            holder.setImageProfile(eventRequest.getToUser().getPhoto().getPhoto());
            setText(holder.textUserName, linkClick, fromUser, WANT_JOIN);
        } else if (eventRequest.getFromUser().getId() == userProfile.getId() && eventRequest.getFromUser().getPhoto() != null) {
            holder.setImageProfile(eventRequest.getFromUser().getPhoto().getPhoto());
            setText(holder.textUserName, linkClick, toUser, SEND_REQUEST);
        }

        holder.textCreated.setText(parseCreated(eventRequest.getCreated()));

        if (eventRequest.getToUser().getId() == userProfile.getId()) {
            holder.decisionButtons.setVisibility(View.VISIBLE);
            if (eventRequest.getDecision().equals("ACCEPT")) {
                holder.decisionButtons.setVisibility(View.GONE);
                setText(holder.textUserName, linkClick, fromUser, ACCEPT);
            } else if (eventRequest.getDecision().equals("DECLINE")) {
                holder.decisionButtons.setVisibility(View.GONE);
                setText(holder.textUserName, linkClick, toUser, DECLINE);
            }


        } else if (eventRequest.getFromUser().getId() == userProfile.getId()) {
            holder.decisionButtons.setVisibility(View.GONE);
            if (eventRequest.getDecision().equals("ACCEPT")) {
                setText(holder.textUserName, linkClick, toUser, ACCEPTED);
            } else if (eventRequest.getDecision().equals("DECLINE")) {
                setText(holder.textUserName, linkClick, toUser, DECLINED);
            }
        }

        holder.acceptButton.setOnClickListener(v -> answerRequest(eventRequest, "ACCEPT"));
        holder.declineButton.setOnClickListener(v -> answerRequest(eventRequest, "DECLINE"));
    }

    private void setText(TextView textView, ClickableSpan linkClick, String username, String supportText) {
        Spannable spannableString = new SpannableString(username + supportText);
        spannableString.setSpan(linkClick, 0, username.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString, TextView.BufferType.SPANNABLE);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void answerRequest(RequestGet eventRequest, String decision) {
        Call<RequestGet> call = RetrofitClient
                .getInstance(PreferenceUtils.getToken(mContext))
                .getApi()
                .answerRequest(String.valueOf(eventRequest.getId()), new RequestSend(decision));

        call.enqueue(new Callback<RequestGet>() {
            @Override
            public void onResponse(@NonNull Call<RequestGet> call, @NonNull Response<RequestGet> response) {
                if (response.body() != null) {
                    eventRequest.setDecision(decision);
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RequestGet> call, @NonNull Throwable t) {
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
        TextView textUserName;

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
