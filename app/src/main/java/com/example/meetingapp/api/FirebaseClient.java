package com.example.meetingapp.api;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.meetingapp.NotificationListener;
import com.example.meetingapp.adapters.NotificationsAdapter;
import com.example.meetingapp.models.Chat;
import com.example.meetingapp.models.EventRequest;
import com.example.meetingapp.models.UserProfile;
import com.example.meetingapp.utils.PreferenceUtils;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirebaseClient {

    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseInstanceId firebaseInstanceId;
    private Context context;

    public FirebaseClient(Context context) {
        this.context = context;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseInstanceId = FirebaseInstanceId.getInstance();
    }

    public String getUid() {
        return firebaseUser.getUid();
    }

//    public void createFirebaseUser(String firebaseUid, String email) {
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUid);
//
//        HashMap<String, String> hashMap = new HashMap<>();
//        hashMap.put("firebaseUid", firebaseUid);
//        hashMap.put("email", email);
//        hashMap.put("imageUrl", "default");
//
//        databaseReference.setValue(hashMap).addOnCompleteListener(Task::isSuccessful);
//    }

    public void login(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int a = 5;
                    } else {
                        Toast.makeText(getContext(), "Auth failed!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void changePassword(String currentPassword, String newPassword) {
        String email = PreferenceUtils.getEmail(getContext());
        AuthCredential credential = EmailAuthProvider.getCredential("maxim137@mail.ru", currentPassword);

        firebaseUser.reauthenticate(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        firebaseUser.updatePassword(newPassword).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                int a = 5;
                            } else {
                                int a = 5;
                            }
                        });
                    } else {
                        int a = 5;
                    }
                });
    }

    public void logout() {
        firebaseAuth.signOut();
    }

    public void saveRegistrationToken() {
        firebaseInstanceId.getInstanceId().addOnSuccessListener(instanceIdResult -> {
            UserProfile userProfile = new UserProfile();
//            userProfile.setFirebaseToken(instanceIdResult.getToken());

            Call<UserProfile> call = RetrofitClient
                    .getInstance(PreferenceUtils.getToken(getContext()))
                    .getApi()
                    .updateProfile(userProfile);

            call.enqueue(new Callback<UserProfile>() {
                @Override
                public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {

                }

                @Override
                public void onFailure(Call<UserProfile> call, Throwable t) {

                }
            });
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

    public void createChat(String sender, String receiver){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);

        databaseReference.child("Chat").push().setValue(hashMap);
    }

    public void createEventChat(long eventId){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("eventId", eventId);

        databaseReference.child("EventChat").push().setValue(hashMap);
    }

    public void sendMessage(Long eventId, String text, String date, String time) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("Chat");

//        // Find users chat
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Chat chat = snapshot.getValue(Chat.class);
//                    if (chat != null && (chat.getReceiver().equals("") && chat.getSender().equals("") ||
//                            chat.getSender().equals("") && chat.getReceiver().equals(""))) {
//
//                        HashMap<String, Object> hashMap = new HashMap<>();
//                        hashMap.put("chat", snapshot.getKey());
//                        hashMap.put("text", text);
//                        hashMap.put("date", date);
//                        hashMap.put("time", time);
//
//                        databaseReference.child("Message").push().setValue(hashMap);
//                    }
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

    private Context getContext() {
        return context;
    }

    private void saveToken(String token) {

    }
}
