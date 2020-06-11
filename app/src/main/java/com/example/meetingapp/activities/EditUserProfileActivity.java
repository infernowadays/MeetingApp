package com.example.meetingapp.activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.meetingapp.R;
import com.example.meetingapp.api.RetrofitClient;
import com.example.meetingapp.models.UserProfile;
import com.example.meetingapp.utils.PreferenceUtils;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditUserProfileActivity extends AppCompatActivity {

    @BindView(R.id.text_city)
    MaterialEditText textCity;

    @BindView(R.id.text_education)
    MaterialEditText textEducation;

    @BindView(R.id.text_job)
    MaterialEditText textJob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button_update_user_profile)
    void updateUserProfile() {
        UserProfile userProfile = new UserProfile();
        userProfile.setCity(Objects.requireNonNull(textCity.getText()).toString());
        userProfile.setEducation(Objects.requireNonNull(textEducation.getText()).toString());
        userProfile.setJob(Objects.requireNonNull(textJob.getText()).toString());

        Call<UserProfile> call = RetrofitClient
                .getInstance(PreferenceUtils.getToken(getContext()))
                .getApi()
                .updateProfile(userProfile);

        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(@NonNull Call<UserProfile> call, @NonNull Response<UserProfile> response) {
                finish();
            }

            @Override
            public void onFailure(@NonNull Call<UserProfile> call, @NonNull Throwable t) {
                Log.d("error", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    private Context getContext() {
        return this;
    }
}
