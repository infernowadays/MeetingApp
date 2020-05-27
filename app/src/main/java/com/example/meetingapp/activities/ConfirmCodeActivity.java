package com.example.meetingapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.meetingapp.R;
import com.goodiebag.pinview.Pinview;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConfirmCodeActivity extends AppCompatActivity {

    @BindView(R.id.pin_view_confirm)
    Pinview pinview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_code);
        ButterKnife.bind(this);

        pinview.setActivated(true);
        pinview.setPressed(true);
        pinview.setPinViewEventListener((pinview, fromUser) -> {

        });
    }
}
