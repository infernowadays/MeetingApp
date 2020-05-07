package com.example.meetingapp.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.meetingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKList;


import com.example.meetingapp.api.FirebaseClient;
import java.util.Objects;


public class LALALALALAActivity extends AppCompatActivity {

    private static final String TAG = "LALALALALAActivity";
    private String[] scope = new String[]{VKScope.EMAIL, VKScope.PHOTOS, VKApiConst.BIRTH_DAY};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFirebase();

//        if (!VKSdk.isLoggedIn()) {
//            VKSdk.login(this, scope);
//        } else {
//            //user авторизован
//            VKSdk.logout();
//            VKSdk.login(this, scope);
//
//            int a = 5;
//        }


//        String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());
//        System.out.println("lalala");
//        System.out.println(Arrays.asList(fingerprints));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                Toast.makeText(getApplicationContext(), "Good", Toast.LENGTH_SHORT).show();


                VKRequest yourRequest = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, "city, country, bdate, photo_50"));
                yourRequest.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);

                        VKApiUser user = ((VKList<VKApiUser>) response.parsedModel).get(0);
                        Log.d("User name", user.first_name + " " + user.last_name);
                    }
                });
                //Get user info
//                VKApi.users().get().executeWithListener(new VKRequest.VKRequestListener() {
//                    @Override
//                    public void onComplete(VKResponse response) {
//                        VKApiUser user = ((VKList<VKApiUser>) response.parsedModel).get(0);
//                        Log.d("User name", user.first_name + " " + user.last_name);
//                    }
//                });
            }

            @Override
            public void onError(VKError error) {
                Toast.makeText(getApplicationContext(), "ЧОРТ", Toast.LENGTH_SHORT).show();
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


//
    private void initFirebase() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel =
                    new NotificationChannel("MyNotifications", "MyNotifications", NotificationManager.IMPORTANCE_HIGH);

            NotificationManager manager = getSystemService(NotificationManager.class);
            assert manager != null;
            manager.createNotificationChannel(channel);
        }

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        FirebaseClient firebaseClient = new FirebaseClient(getApplicationContext());
                        String uid = firebaseClient.getUid();

                        firebaseClient.logout();

                        // Get new Instance ID token
                        String token = Objects.requireNonNull(task.getResult()).getToken();

//                        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
//                            @Override
//                            public void onSuccess(InstanceIdResult instanceIdResult) {
//                                String newToken = instanceIdResult.getToken();
//
//                            }
//                        });

                        // Log and toast
                        String msg = "ok";
                        Log.d(TAG, msg);
                        Toast.makeText(LALALALALAActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });

//        FirebaseMessaging.getInstance().subscribeToTopic("general")
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        String msg = "ok";
//                        if (!task.isSuccessful()) {
//                            msg = "failed";
//                        }
//                        Log.d(TAG, msg);
//                        Toast.makeText(LALALALALAActivity.this, msg, Toast.LENGTH_SHORT).show();
//                    }
//                });
    }

}