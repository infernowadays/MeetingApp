package com.example.meetingapp.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.meetingapp.services.AuthService;
import com.example.meetingapp.R;
import com.example.meetingapp.api.RetrofitClient;
import com.example.meetingapp.services.NetworkConnection;
import com.example.meetingapp.utils.PreferenceUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StartActivity extends AppCompatActivity {

    @BindView(R.id.text_email)
    EditText textEmail;
    @BindView(R.id.text_password)
    EditText textPassword;
    @BindView(R.id.layout_logo)
    LinearLayout layoutLogo;
    @BindView(R.id.layout_start)
    LinearLayout layoutStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);

        loadApp();
    }

    @OnClick(R.id.button_login)
    public void login() {
       boolean isNetworkOnlineLogin = new NetworkConnection(this).isNetworkOnline(this);
        if (isNetworkOnlineLogin) {
        login(textEmail.getText().toString(), textPassword.getText().toString());}
        else {Toast.makeText(StartActivity.this, "Произошла сетевая ошибка. Проверьте что подключение к интернет работает стабильно.", Toast.LENGTH_SHORT).show();}
    }

    private void login(String email, String password) {
        RetrofitClient.needsHeader(false);

        AuthService authService = new AuthService(getContext());
        authService.authenticate(email, password);
//        authService.finishAuth();
    }
    //Boolean isNetworkOnline = new NetworkConnection(this).isNetworkOnline(this);

    @OnClick(R.id.button_register)
    void createAccount() {
        boolean isNetworkOnlineRegister = new NetworkConnection(this).isNetworkOnline(this);
        if (isNetworkOnlineRegister) {
            startActivity(new Intent(StartActivity.this, RegisterActivity.class));}
        else {Toast.makeText(StartActivity.this, "Произошла сетевая ошибка. Проверьте что подключение к интернет работает стабильно.", Toast.LENGTH_SHORT).show();}


    }

   // Boolean isNetworkOnline = new NetworkConnection(this).isNetworkOnline(this);

    private void loadApp() {
        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            if (PreferenceUtils.hasToken(this)) {
                if (PreferenceUtils.isFilled(this)) {
                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(StartActivity.this, CreateUserProfileActivity.class);
                    startActivity(intent);
                    finish();
                }
            } else {
                layoutLogo.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                layoutStart.setVisibility(View.VISIBLE);
            }
        }, 1000);
    }

    private Context getContext() {
        return this;
    }
}
