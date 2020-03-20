package com.example.meetingapp;

import android.os.Handler;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MyHandler {
    private static Handler handler;
    private static Runnable mUpdateTimeTask = () -> status("offline");
    private static FirebaseUser firebaseUser;


    public static Handler getHandler() {
        if (handler == null) {
            initHandler();
        }
        return handler;
    }

    public static void setUser(FirebaseUser firebaseUser){
        MyHandler.firebaseUser = firebaseUser;
    }

    private static void initHandler() {
        handler = new Handler();
        handler.postDelayed(mUpdateTimeTask,5000);
    }

    public static void stopMyHandler() {
        handler.removeCallbacksAndMessages(null);
    }

    public static void pauseMyHandler(Runnable myRunnable) {
        handler.removeCallbacksAndMessages(myRunnable);
    }

    public static void resumeMyHandler(Runnable myRunnable) {
        handler.postDelayed(myRunnable,5000);
    }

    private static void status(String status) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        databaseReference.updateChildren(hashMap);
    }
}