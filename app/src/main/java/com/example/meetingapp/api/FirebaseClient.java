package com.example.meetingapp.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.meetingapp.activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class FirebaseClient {

    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private Context context;

    public FirebaseClient(Context context) {
        this.context = context;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public String getUid() {
        return firebaseUser.getUid();
    }

    public void createFirebaseUser(String firebaseUid, String email) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUid);

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("firebaseUid", firebaseUid);
        hashMap.put("email", email);
        hashMap.put("imageUrl", "default");

        databaseReference.setValue(hashMap).addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()) {

            }
        });
    }

    public void login(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        ((Activity) context).finish();
                    } else {
                        Toast.makeText(context, "Auth failed!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void sendRequest(String toUser, long event) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("fromUser", firebaseUser.getUid());
        hashMap.put("toUser", toUser);
        hashMap.put("event", event);
        hashMap.put("decision", "NO_ANSWER");
        hashMap.put("seen", false);

        databaseReference.child("Request").push().setValue(hashMap);
    }

    public void sendMessage(Long eventId, String message, String date, String time) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("firebaseUid", firebaseUser.getUid());
        hashMap.put("eventId", eventId);
        hashMap.put("message", message);
        hashMap.put("date", date);
        hashMap.put("time", time);

        databaseReference.child("Message").push().setValue(hashMap);
    }
}
