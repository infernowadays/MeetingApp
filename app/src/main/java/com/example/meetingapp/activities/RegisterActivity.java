package com.example.meetingapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.meetingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    MaterialEditText email, username, password;
    Button btn_register;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        email = findViewById(R.id.email);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        btn_register = findViewById(R.id.registerButton);

        firebaseAuth = FirebaseAuth.getInstance();

        btn_register.setOnClickListener(v -> {
            String  txt_email = Objects.requireNonNull(email.getText()).toString();
            String txt_username = Objects.requireNonNull(username.getText()).toString();
            String  txt_password = Objects.requireNonNull(password.getText()).toString();

            if(TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password))
                Toast.makeText(RegisterActivity.this, "Заполни все поля!", Toast.LENGTH_SHORT).show();
            else if(txt_password.length() < 6)
                Toast.makeText(RegisterActivity.this, "Слишком короткий пароль", Toast.LENGTH_SHORT).show();
            else
                register(txt_username, txt_email, txt_password);
        });
    }

    private void register(String username, String email, String password){
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        assert firebaseUser != null;
                        String user_id = firebaseUser.getUid();

                        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user_id);

                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("id", user_id);
                        hashMap.put("username", username);
                        hashMap.put("imageURL", "default");

                        databaseReference.setValue(hashMap).addOnCompleteListener(task1 -> {
                            if(task1.isSuccessful()){
                                Intent intent = new Intent(RegisterActivity.this, Main2Activity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        });
                    } else{
                        Toast.makeText(RegisterActivity.this, "Provide all the data", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
