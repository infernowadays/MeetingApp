package com.example.meetingapp.activities;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.meetingapp.services.AuthService;
import com.example.meetingapp.customviews.CustomCallback;
import com.example.meetingapp.R;
import com.example.meetingapp.api.RetrofitClient;
import com.example.meetingapp.models.RegisterData;
import com.example.meetingapp.models.UserProfile;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.text_email)
    EditText textEmail;
    @BindView(R.id.text_first_name)
    EditText textFirstName;
    @BindView(R.id.text_last_name)
    EditText textLastName;
    @BindView(R.id.text_password)
    EditText textPassword;

    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.registerButton)
    public void register() {
        String email = Objects.requireNonNull(textEmail.getText()).toString();
        String firstName = Objects.requireNonNull(textFirstName.getText()).toString();
        String lastName = Objects.requireNonNull(textLastName.getText()).toString();
        String password = Objects.requireNonNull(textPassword.getText()).toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName)
                || TextUtils.isEmpty(password))
            Toast.makeText(RegisterActivity.this, "Заполни все поля!", Toast.LENGTH_SHORT).show();
        else if (password.length() < 6)
            Toast.makeText(RegisterActivity.this, "Слишком короткий пароль", Toast.LENGTH_SHORT).show();
        else
            register(email, firstName, lastName, password);
    }

    private void register(String email, String firstName, String lastName, String password) {
        RetrofitClient.needsHeader(false);

        Call<UserProfile> call = RetrofitClient
                .getInstance("")
                .getApi()
                .users(new RegisterData(email, firstName, lastName, password));

        call.enqueue(new CustomCallback<UserProfile>(getContext()) {
            @Override
            public void onResponse(@NonNull Call<UserProfile> call, @NonNull Response<UserProfile> response) {
                super.onResponse(call, response);

                if (response.isSuccessful() && response.body() != null) {
                    AuthService authService = new AuthService(getContext());
                    authService.authenticate(email, password);

                } else {
                    Toast.makeText(RegisterActivity.this, ":(", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserProfile> call, @NonNull Throwable t) {
                super.onFailure(call, t);
                Toast.makeText(RegisterActivity.this, "Ошибка подключения к интернету", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public Context getContext() {
        return mContext;
    }
}
