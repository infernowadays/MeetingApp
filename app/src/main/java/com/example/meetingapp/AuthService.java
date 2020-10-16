package com.example.meetingapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.example.meetingapp.activities.MainActivity;
import com.example.meetingapp.api.RetrofitClient;
import com.example.meetingapp.models.LoginData;
import com.example.meetingapp.models.Token;
import com.example.meetingapp.utils.PreferenceUtils;

import retrofit2.Call;
import retrofit2.Response;

public class AuthService {

    private Context context;

    public AuthService(Context c){
        context = c;
    }

    public void authenticate(String email, String password){
        Call<Token> call = RetrofitClient
                .getInstance( PreferenceUtils.getToken(getContext()))
                .getApi()
                .login(new LoginData(email, password));

        call.enqueue(new CustomCallback<Token>(getContext()) {
            @Override
            public void onResponse(@NonNull Call<Token> call, @NonNull Response<Token> response) {
                super.onResponse(call, response);

                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        PreferenceUtils.saveToken(response.body().getToken(), getContext());
                        RetrofitClient.needsHeader(true);
                        RetrofitClient.setToken(response.body().getToken());
                    }
                } else {
                    int a = 6;
//                        Toast.makeText(LoginActivity.this, "Убедись, что ввели данные корректно.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Token> call, @NonNull Throwable t) {
//                Toast.makeText(LoginActivity.this, "Нет соединения с интернетом :(", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void finishAuth(){
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        getContext().startActivity(intent);
        ((Activity) getContext()).finish();
    }

    private Context getContext(){
        return context;
    }

}
