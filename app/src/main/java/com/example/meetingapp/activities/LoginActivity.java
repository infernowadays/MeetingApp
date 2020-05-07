package com.example.meetingapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.meetingapp.AuthService;
import com.example.meetingapp.R;
import com.example.meetingapp.api.FirebaseClient;
import com.example.meetingapp.api.RetrofitClient;
import com.example.meetingapp.models.LoginData;
import com.example.meetingapp.models.Token;
import com.example.meetingapp.models.UserProfile;
import com.example.meetingapp.utils.PreferenceUtils;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    static final String BASE_URL = "http://10.0.2.2:8000/";

    @BindView(R.id.editTextEmail)
    public EditText editTextEmail;

    @BindView(R.id.editTextPassword)
    public EditText editTextPassword;

    private Context mContext = this;
    private static LoginActivity instance;
    private FirebaseClient firebaseClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        firebaseClient = new FirebaseClient(getContext());

//
//        if (PreferenceUtils.getEmail(this)){
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(intent);
//
//        }

//        editTextEmail = findViewById(R.id.editTextEmail);
//        editTextPassword = findViewById(R.id.editTextPassword);
//
//        findViewById(R.id.buttonLogin).setOnClickListener((view) -> {
//            login();
//        });
//        findViewById(R.id.button).setOnClickListener((view) -> { users(); });

    }

    public static LoginActivity getInstance(){
        return instance;
    }

    @OnClick(R.id.buttonLogin)
    public void buttonLogin() {
        login(editTextEmail.getText().toString(), editTextPassword.getText().toString());
    }


    private String getToken() {
        return PreferenceUtils.getToken(this);
    }

    private void saveToken(String token) {
        PreferenceUtils.saveToken(token, getContext());
    }

    public void login(String email, String password) {
        RetrofitClient.needsHeader(false);

        Toast.makeText(LoginActivity.this, "Входим в профиль...", Toast.LENGTH_SHORT).show();
        AuthService authService = new AuthService(getContext());
        authService.authenticate(email, password);
//        firebaseClient.saveRegistrationToken();
//        firebaseClient.login(email, password);
//
//        Call<Token> call = RetrofitClient
//                .getInstance(getToken())
//                .getApi()
//                .login(new LoginData(email, password));
//
//        call.enqueue(new Callback<Token>() {
//            @Override
//            public void onResponse(Call<Token> call, Response<Token> response) {
//                if (response.isSuccessful()) {
//                    if (response.body() != null) {
//
//
//                        PreferenceUtils.saveToken(response.body().getToken(), getContext());
//                        RetrofitClient.needsHeader(true);
//                        RetrofitClient.setToken(response.body().getToken());
//
//                        Intent intent = new Intent(getContext(), MainActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        getContext().startActivity(intent);
//                        ((Activity) getContext()).finish();
//
////                        firebaseLogin(editTextEmail.getText().toString(), editTextPassword.getText().toString());
////                        PreferenceUtils.saveToken(response.body().getToken(), getContext());
//
//
////                        Intent intent = new Intent(getContext(), MainActivity.class);
////                        startActivity(intent);
////                        finish();
//                    }
//                } else {
////                    Toast.makeText(LoginActivity.this, "Убедись, что ввели данные корректно.", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Token> call, Throwable t) {
////                Toast.makeText(LoginActivity.this, "Нет соединения с интернетом :(", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    public void firebaseLogin(String txt_email, String txt_password) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

//        String txt_email = Objects.requireNonNull(editTextEmail.getText()).toString();
//        String txt_password = Objects.requireNonNull(editTextPassword.getText()).toString();

        if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password))
            Toast.makeText(LoginActivity.this, "Заполни все поля!", Toast.LENGTH_SHORT).show();
        else
            firebaseAuth.signInWithEmailAndPassword(txt_email, txt_password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Auth failed!", Toast.LENGTH_SHORT).show();
                        }
                    });
    }


//    private void login() {
//        // String email = editTextEmail.getText().toString().trim();
//        // String password = editTextPassword.getText().toString().trim();
////
////        PreferenceUtils.saveEmail("admin", this);
////        PreferenceUtils.savePassword("admin", this);
//
//
//        Retrofit.Builder builder = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create());
//
//        Retrofit retrofit = builder.build();
//        Api userClient = retrofit.create(Api.class);
//
//        Login login = new Login("admin", "admin");
//        Call<User> call = userClient.login(login);
//
//        call.enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                if (response.isSuccessful()) {
//                    Toast.makeText(LoginActivity.this, response.body().getToken(), Toast.LENGTH_SHORT).show();
//
//                    PreferenceUtils.saveToken(response.body().getToken(), getContext());
//
//                    firebaseLogin();
////                    Intent intent = new Intent(getContext(), MainActivity.class);
////                    startActivity(intent);
////                    finish();
//                } else {
//                    Toast.makeText(LoginActivity.this, "login or password is incorrect :(", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//                Toast.makeText(LoginActivity.this, "error :(", Toast.LENGTH_SHORT).show();
//                // t.fillInStackTrace();
//
//                Intent intent = new Intent(getContext(), MainActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });
//
//    }

//    private void users() {
//        // String email = editTextEmail.getText().toString().trim();
//        // String password = editTextPassword.getText().toString().trim();
//
//        Retrofit.Builder builder = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create());
//
//        Retrofit retrofit = builder.build();
//        Api userClient = retrofit.create(Api.class);
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
        return mContext;
    }
}
