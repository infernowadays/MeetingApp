package com.example.meetingapp.activities;

import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.meetingapp.CustomCallback;
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
        String currentPassword = textCurrentPassword.getText().toString();
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
        });

    }

    private Context getContext() {
        return this;
    }
}
