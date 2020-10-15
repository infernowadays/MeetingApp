package com.example.meetingapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.meetingapp.activities.CreateUserProfileActivity;
import com.example.meetingapp.activities.MainActivity;
import com.example.meetingapp.api.RetrofitClient;
import com.example.meetingapp.models.LoginData;
import com.example.meetingapp.models.Token;
import com.example.meetingapp.models.UserProfile;
import com.example.meetingapp.utils.PreferenceUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthService {

    private Context context;

    public AuthService(Context c) {
        context = c;
    }

    public void authenticate(String email, String password) {
        Call<Token> call = RetrofitClient
                .getInstance(PreferenceUtils.getToken(getContext()))
                .getApi()
                .login(new LoginData(email, password));

        call.enqueue(new CustomCallback<Token>(getContext()) {
            @Override
            public void onResponse(@NonNull Call<Token> call, @NonNull Response<Token> response) {
                super.onResponse(call, response);

                if (response.isSuccessful() && response.body() != null) {

                    PreferenceUtils.saveToken(response.body().getToken(), getContext());
                    RetrofitClient.needsHeader(true);
                    RetrofitClient.setToken(response.body().getToken());

                    meProfile();
                } else {
                    Toast.makeText(context, "Пользователь с такими данными не найден", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Token> call, @NonNull Throwable t) {
//                Toast.makeText(LoginActivity.this, "Нет соединения с интернетом :(", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void finishAuth() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        getContext().startActivity(intent);
        ((Activity) getContext()).finish();
    }

    private void meProfile() {
        Call<UserProfile> call = RetrofitClient
                .getInstance(PreferenceUtils.getToken(getContext()))
                .getApi()
                .meProfile();

        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(@NonNull Call<UserProfile> call, @NonNull Response<UserProfile> response) {
                if (response.body() != null) {
                    UserProfile userProfile = response.body();
                    UserProfileManager.getInstance().initialize(userProfile);
                    PreferenceUtils.saveUserId(userProfile.getId(), getContext());

                    if (!userProfile.getFilled()) {
                        Intent intent = new Intent(getContext(), CreateUserProfileActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        getContext().startActivity(intent);
                        ((Activity) getContext()).finish();
                    } else {
                        PreferenceUtils.saveFilled(true, getContext());

                        Intent intent = new Intent(getContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                        getContext().startActivity(intent);
                        ((Activity) getContext()).finish();
                    }

                }


            }

            @Override
            public void onFailure(@NonNull Call<UserProfile> call, @NonNull Throwable t) {

            }
        });
    }

    private Context getContext() {
        return context;
    }
}
