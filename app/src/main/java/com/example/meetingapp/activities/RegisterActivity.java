package com.example.meetingapp.activities;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.meetingapp.AuthService;
import com.example.meetingapp.CustomCallback;
import com.example.meetingapp.R;
import com.example.meetingapp.api.FirebaseClient;
import com.example.meetingapp.api.RetrofitClient;
import com.example.meetingapp.models.RegisterData;
import com.example.meetingapp.models.UserProfile;
import com.example.meetingapp.utils.PreferenceUtils;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.email)
    public EditText email;
    @BindView(R.id.username)
    public EditText username;
    @BindView(R.id.password)
    public EditText password;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    private FirebaseClient firebaseClient;
    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        firebaseClient = new FirebaseClient(getContext());

        initToolbar();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @OnClick(R.id.registerButton)
    public void registerButton() {
        String txt_email = Objects.requireNonNull(email.getText()).toString();
        String txt_username = Objects.requireNonNull(username.getText()).toString();
        String txt_password = Objects.requireNonNull(password.getText()).toString();

        if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password))
            Toast.makeText(RegisterActivity.this, "Заполни все поля!", Toast.LENGTH_SHORT).show();
        else if (txt_password.length() < 6)
            Toast.makeText(RegisterActivity.this, "Слишком короткий пароль", Toast.LENGTH_SHORT).show();
        else
            register(txt_username, txt_email, txt_password);
    }

    private void register(String username, String email, String password) {
        RetrofitClient.needsHeader(false);


        Toast.makeText(RegisterActivity.this, "Создаем аккаунт...", Toast.LENGTH_SHORT).show();

        Call<UserProfile> call = RetrofitClient
                .getInstance(PreferenceUtils.getToken(getContext()))
                .getApi()
                .users(new RegisterData(email, username, password));

        call.enqueue(new CustomCallback<UserProfile>(getContext()) {
            @Override
            public void onResponse(@NonNull Call<UserProfile> call, @NonNull Response<UserProfile> response) {
                super.onResponse(call, response);

                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(RegisterActivity.this, "Аккаунт создан!", Toast.LENGTH_SHORT).show();

                    UserProfile userProfile = response.body();

//                    PreferenceUtils.saveToken(userProfile.getFirebaseToken(), getContext());
//                    RetrofitClient.setToken(userProfile.getFirebaseToken());
//                    RetrofitClient.needsHeader(true);

//                    firebaseClient.createFirebaseUser(userProfile.getFirebaseUid(), email);


                    AuthService authService = new AuthService(getContext());
                    authService.authenticate(email, password);


                } else {
                    Toast.makeText(RegisterActivity.this, ":(", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserProfile> call, @NonNull Throwable t) {
                Toast.makeText(RegisterActivity.this, "error :(", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public Context getContext() {
        return mContext;
    }
}
