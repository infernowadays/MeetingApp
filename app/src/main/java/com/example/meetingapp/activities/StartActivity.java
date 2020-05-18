package com.example.meetingapp.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.meetingapp.R;
import com.example.meetingapp.UserProfileManager;
import com.example.meetingapp.api.RetrofitClient;
import com.example.meetingapp.models.UserProfile;
import com.example.meetingapp.utils.PreferenceUtils;
import com.google.firebase.auth.FirebaseUser;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StartActivity extends AppCompatActivity {

    FirebaseUser firebaseUser;
    UserProfile userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);

        userProfile = null;
        meProfile();
//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        firebaseUser != null &&


    }

    @OnClick(R.id.button_login)
    void login() {
        startActivity(new Intent(StartActivity.this, LoginActivity.class));
    }

    @OnClick(R.id.button_register)
    void createAccount() {
        startActivity(new Intent(StartActivity.this, RegisterActivity.class));
    }

    private void meProfile() {
        Call<UserProfile> call = RetrofitClient
                .getInstance(PreferenceUtils.getToken(this))
                .getApi()
                .meProfile();

        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(@NonNull Call<UserProfile> call, @NonNull Response<UserProfile> response) {
                if (response.body() != null) {
                    userProfile = response.body();
                    UserProfileManager.getInstance().initialize(userProfile);
                    checkPrerequisites();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserProfile> call, @NonNull Throwable t) {

            }
        });
    }

    private void checkPrerequisites() {
        if (userProfile.getFilled() && PreferenceUtils.hasToken(this)) {
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else if (!userProfile.getFilled()) {
            Intent intent = new Intent(StartActivity.this, UserProfileStepperActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
