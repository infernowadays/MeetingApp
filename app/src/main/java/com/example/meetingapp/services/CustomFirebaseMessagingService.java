package com.example.meetingapp.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.meetingapp.R;
import com.example.meetingapp.activities.EventActivity;
import com.example.meetingapp.utils.PreferenceUtils;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class CustomFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    public static Bitmap textAsBitmap(Context context, String messageText, String text, float textSize, int textColor) {
        Paint paint = new Paint();
        paint.setTextSize(textSize);

        paint.setColor(textColor);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(messageText) + 0.5f); // round
        int height = (int) (baseline + paint.descent() + 0.5f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        PreferenceUtils.saveFirebaseToken(s, this);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getData().size() > 0)
            showNotification(remoteMessage.getData().get("sender"), remoteMessage.getData().get("message"), remoteMessage.getData().get("chat_id"));
    }

    public void showNotification(String sender, String message, String chatId) {
        Spannable sb = new SpannableString(sender);
        sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, sender.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        Intent intent = new Intent(this, EventActivity.class);
        intent.putExtra("EXTRA_EVENT_ID", String.valueOf(chatId));

        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification builder = new NotificationCompat.Builder(this, "PushNotifications")
                .setContentTitle(sb)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_wolf)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_edit_icon, "Прочитать", pIntent)
                .setContentIntent(pIntent)
                .build();

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("PushNotifications", "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            if (manager != null) {
                manager.createNotificationChannel(channel);
                manager.notify(new Random().nextInt(100), builder);
            }
        }
    }
}