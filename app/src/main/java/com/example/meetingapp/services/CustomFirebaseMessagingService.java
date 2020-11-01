package com.example.meetingapp.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.meetingapp.R;
import com.example.meetingapp.activities.EventActivity;
import com.example.meetingapp.activities.StartActivity;
import com.example.meetingapp.api.RetrofitClient;
import com.example.meetingapp.utils.PreferenceUtils;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private final String MESSAGE = "MESSAGE";
    private final String REQUEST = "REQUEST";

    public static void sendFirebaseTokenToServer(String newFirebaseToken, String token) {
        Call<Void> call = RetrofitClient
                .getInstance(token)
                .getApi()
                .updateFirebaseToken(newFirebaseToken);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                Log.d("token send", Objects.requireNonNull("ok"));

            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.d("error", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        PreferenceUtils.saveFirebaseToken(s, this);

        if (PreferenceUtils.hasToken(this))
            sendFirebaseTokenToServer(PreferenceUtils.getFirebaseToken(this), PreferenceUtils.getToken(this));
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        if (remoteMessage.getData().size() > 0) {
            if (Objects.requireNonNull(remoteMessage.getData().get("content_type")).equals(MESSAGE))
                messageNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"), remoteMessage.getData().get("content_id"));

            else if (Objects.requireNonNull(remoteMessage.getData().get("content_type")).equals(REQUEST))
                requestNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"));
        }
    }

    public void requestNotification(String title, String message) {
        Spannable titleBold = new SpannableString(title);
        titleBold.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        Intent intentMainActivity = new Intent(this, StartActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intentMainActivity, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, "bundle_channel_id")
                        .setContentTitle(titleBold)
                        .setSmallIcon(R.drawable.ic_wolf)
                        .setContentIntent(pendingIntent)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                        .setContentText(message);

        Notification notification = builder.build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(new Random().nextInt(100), notification);
        }
    }

    public void messageNotification(String title, String message, String contentId) {
        // Make title bold
        Spannable titleBold = new SpannableString(title);
        titleBold.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Load parent activity with child
        Intent intent = new Intent(this, EventActivity.class);
        intent.putExtra("EXTRA_EVENT_ID", String.valueOf(contentId));

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(EventActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Notification bundle
        int bundleNotificationId = Integer.parseInt(contentId);

        String bundle_notification_id = "bundle_notification_" + contentId;

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder summaryNotificationBuilder;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel groupChannel = new NotificationChannel("bundle_channel_id", "bundle_channel_name", NotificationManager.IMPORTANCE_LOW);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(groupChannel);

                NotificationChannel channel = new NotificationChannel("channel_id", "channel_name", NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }
        }

        summaryNotificationBuilder = new NotificationCompat.Builder(this, "bundle_channel_id")
                .setGroup(bundle_notification_id)
                .setGroupSummary(true)
                .setSmallIcon(R.drawable.ic_wolf)
                .setContentIntent(pendingIntent)
                .setContentTitle("Сообщения");

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, "channel_id")
                .setGroup(bundle_notification_id)
                .setContentTitle(titleBold)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_wolf)
                .setGroupSummary(false);

        if (notificationManager != null) {
            notificationManager.notify(new Random().nextInt(), notification.build());
            notificationManager.notify(bundleNotificationId, summaryNotificationBuilder.build());
        }
    }
}