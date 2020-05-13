package com.example.meetingapp.activities;

import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.meetingapp.AuthService;
import com.example.meetingapp.R;
import com.example.meetingapp.api.RetrofitClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.text_email)
    public EditText textEmail;

    @BindView(R.id.text_password)
    public EditText textPassword;

    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button_login)
    public void login() {
        login(textEmail.getText().toString(), textPassword.getText().toString());
    }

    private void login(String email, String password) {
        RetrofitClient.needsHeader(false);

        Toast.makeText(LoginActivity.this, "Входим в профиль...", Toast.LENGTH_SHORT).show();
        AuthService authService = new AuthService(getContext());
        authService.authenticate(email, password);
    }

    private Context getContext() {
        return context;
    }
}
