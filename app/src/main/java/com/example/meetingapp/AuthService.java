package com.example.meetingapp;

import androidx.annotation.NonNull;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.meetingapp.activities.MainActivity;
import com.example.meetingapp.api.FirebaseClient;
import com.example.meetingapp.api.RetrofitClient;
import com.example.meetingapp.models.LoginData;
import com.example.meetingapp.models.Token;
import com.example.meetingapp.utils.PreferenceUtils;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

public class AuthService {

    private FirebaseClient firebaseClient;
    private Context context;

    public AuthService(Context c){
        context = c;
        firebaseClient =  new FirebaseClient(getContext());
    }

    public void authenticate(String email, String password){
        firebaseClient.login(email, password);

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


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

                        firebaseClient.saveRegistrationToken();

                        Intent intent = new Intent(getContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                        getContext().startActivity(intent);
                        ((Activity) getContext()).finish();

                    }
                } else {
//                    Toast.makeText(LoginActivity.this, "Убедись, что ввели данные корректно.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Token> call, @NonNull Throwable t) {
//                Toast.makeText(LoginActivity.this, "Нет соединения с интернетом :(", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Context getContext(){
        return context;
    }
}
