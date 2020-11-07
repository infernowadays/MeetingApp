package com.example.meetingapp.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.meetingapp.customviews.CustomCallback;
import com.example.meetingapp.R;
import com.example.meetingapp.api.RetrofitClient;
import com.example.meetingapp.models.Password;
import com.example.meetingapp.models.Token;
import com.example.meetingapp.utils.PreferenceUtils;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    @BindView(R.id.text_current_password)
    EditText textCurrentPassword;

    @BindView(R.id.text_new_password)
    EditText textNewPassword;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.text)
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        text.setText("Изменить пароль");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @OnClick(R.id.button_change_password)
    void changePassword() {
        if (isNetworkOnline(this)) {String currentPassword = textCurrentPassword.getText().toString();
            String newPassword = textNewPassword.getText().toString();

            Call<Token> call = RetrofitClient
                    .getInstance(PreferenceUtils.getToken(getContext()))
                    .getApi()
                    .changePassword(new Password(currentPassword, newPassword));

            call.enqueue(new CustomCallback<Token>(getContext()) {
                @Override
                public void onResponse(@NonNull Call<Token> call, @NonNull Response<Token> response) {
                    super.onResponse(call, response);

                    if (response.isSuccessful() && response.body() != null) {
                        int a = 5;


                    } else {
                        int a = 5;
                        // wrong password

                    }
                }

                @Override
                public void onFailure(@NonNull Call<Token> call, @NonNull Throwable t) {
                    int a = 5;

                }
            });}
        else {
            Toast.makeText(ChangePasswordActivity.this, "Произошла сетевая ошибка. Проверьте что подключение к интернет работает стабильно.", Toast.LENGTH_SHORT).show();}



    }

    private Context getContext() {
        return this;
    }

    public boolean isNetworkOnline(Context context) {
        boolean status = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                status = true;
            } else {
                netInfo = cm.getNetworkInfo(1);
                if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED)
                    status = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return status;

    }
}
