package com.example.meetingapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.meetingapp.R;
import com.example.meetingapp.api.DjangoClient;
import com.example.meetingapp.models.Login;
import com.example.meetingapp.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    static final String BASE_URL = "http://10.0.2.2:8000/";
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//
//        if (PreferenceUtils.getEmail(this)){
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(intent);
//
//        }

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        findViewById(R.id.buttonLogin).setOnClickListener((view) -> {
            login();
        });
//        findViewById(R.id.button).setOnClickListener((view) -> { users(); });

    }

//    @Override
//    public void onBackPressed() {
////        moveTaskToBack(true);
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if(keyCode== KeyEvent.KEYCODE_BACK) {
//            return false;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    private void login() {
        // String email = editTextEmail.getText().toString().trim();
        // String password = editTextPassword.getText().toString().trim();
//
//        PreferenceUtils.saveEmail("admin", this);
//        PreferenceUtils.savePassword("admin", this);


        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();
        DjangoClient userClient = retrofit.create(DjangoClient.class);

        Login login = new Login("admin", "admin");
        Call<User> call = userClient.login(login);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, response.body().getToken(), Toast.LENGTH_SHORT).show();


                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "login or password is incorrect :(", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "error :(", Toast.LENGTH_SHORT).show();
                // t.fillInStackTrace();

                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

//    private void users() {
//        // String email = editTextEmail.getText().toString().trim();
//        // String password = editTextPassword.getText().toString().trim();
//
//        Retrofit.Builder builder = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create());
//
//        Retrofit retrofit = builder.build();
//        DjangoClient userClient = retrofit.create(DjangoClient.class);
//
//        Call<List<User>> call = userClient.getUsers("Token 75e1ebffabf37f98db48544c90ecb57af4053972");
//
//        call.enqueue(new Callback<List<User>>() {
//            @Override
//            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
//                Toast.makeText(LoginActivity.this, String.valueOf(response.body().size()), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailure(Call<List<User>> call, Throwable t) {
//                Toast.makeText(LoginActivity.this, "псы", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    public Context getContext() {
        return context;
    }
}
