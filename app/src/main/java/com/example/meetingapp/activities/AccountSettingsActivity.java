package com.example.meetingapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.meetingapp.R;
import com.example.meetingapp.api.FirebaseClient;
import com.example.meetingapp.utils.PreferenceUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.text_change_password)
    void changePassword() {
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.text_logout)
    void logout() {
        FirebaseClient firebaseClient = new FirebaseClient(getContext());
        firebaseClient.logout();

        PreferenceUtils.removeToken(getContext());
        Intent intent = new Intent(this, StartActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        finish();
    }

    private Context getContext() {
        return this;
    }
}