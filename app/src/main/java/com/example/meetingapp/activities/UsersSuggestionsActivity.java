package com.example.meetingapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.meetingapp.R;

import butterknife.ButterKnife;

public class UsersSuggestionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_suggestions);
        ButterKnife.bind(this);
    }
}
