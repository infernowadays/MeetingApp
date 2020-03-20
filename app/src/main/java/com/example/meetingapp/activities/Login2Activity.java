package com.example.meetingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.meetingapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Objects;

public class Login2Activity extends AppCompatActivity {

    MaterialEditText email, password;
    Button btn_login;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    TextView forgot_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        firebaseAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn_login = findViewById(R.id.loginButton);
        forgot_password = findViewById(R.id.forgot_password);
        forgot_password.setOnClickListener(v -> startActivity(new Intent(Login2Activity.this, ResetPasswordActivity.class)));

        btn_login.setOnClickListener(v -> {
            String txt_email = Objects.requireNonNull(email.getText()).toString();
            String txt_password = Objects.requireNonNull(password.getText()).toString();

            if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password))
                Toast.makeText(Login2Activity.this, "Заполни все поля!", Toast.LENGTH_SHORT).show();
            else
                firebaseAuth.signInWithEmailAndPassword(txt_email, txt_password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(Login2Activity.this, Main2Activity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(Login2Activity.this, "Auth failed!", Toast.LENGTH_SHORT).show();
                            }
                        });
        });
    }
}
