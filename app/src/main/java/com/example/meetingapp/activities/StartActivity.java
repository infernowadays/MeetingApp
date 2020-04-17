package com.example.meetingapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.meetingapp.R;
import com.example.meetingapp.utils.PreferenceUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity {

    Button login, register;
    FirebaseUser firebaseUser;

    @Override
    protected void onStart() {
        super.onStart();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser != null){
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null && PreferenceUtils.hasToken(this)){
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        login = findViewById(R.id.myLoginButton);
        register = findViewById(R.id.myRegisterButton);

        login.setOnClickListener(v -> startActivity(new Intent(StartActivity.this, LoginActivity.class)));
        register.setOnClickListener(v -> startActivity(new Intent(StartActivity.this, RegisterActivity.class)));
    }
}
