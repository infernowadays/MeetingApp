package com.example.meetingapp.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetingapp.R;
import com.example.meetingapp.TransferCategories;
import com.example.meetingapp.adapters.CategoryChipsAdapter;
import com.example.meetingapp.api.RetrofitClient;
import com.example.meetingapp.models.Category;
import com.example.meetingapp.models.MegaCategory;
import com.example.meetingapp.models.UserProfile;
import com.example.meetingapp.utils.PreferenceUtils;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditUserProfileCategoriesActivity extends AppCompatActivity implements TransferCategories {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.categories_counter)
    MaterialButton categoriesCounter;
    private ArrayList<String> stringCategories;
    private CategoryChipsAdapter categoryChipsAdapter;

    @OnClick(R.id.fullscreen_dialog_close)
    void close() {
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile_categories);
        ButterKnife.bind(this);

        stringCategories = new ArrayList<>();
        getCategories();
    }

    @OnClick(R.id.button_update_user_profile)
    void updateUserProfileCategories() {
        UserProfile userProfile = new UserProfile();

        List<Category> categories = new ArrayList<>();
        for (String category : stringCategories) {
            categories.add(new Category(category));
        }

        userProfile.setCategories(categories);


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

    private void getCategories() {
        RetrofitClient.needsHeader(false);
        Call<List<MegaCategory>> call = RetrofitClient
                .getInstance("")
                .getApi()
                .getCategories();

        call.enqueue(new Callback<List<MegaCategory>>() {
            @Override
            public void onResponse(@NonNull Call<List<MegaCategory>> call, @NonNull Response<List<MegaCategory>> response) {
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                categoryChipsAdapter = new CategoryChipsAdapter(getContext(), recyclerView,
                        response.body(), EditUserProfileCategoriesActivity.this, stringCategories);
                recyclerView.setAdapter(categoryChipsAdapter);

                RetrofitClient.needsHeader(true);
            }

            @Override
            public void onFailure(@NonNull Call<List<MegaCategory>> call, @NonNull Throwable t) {
                Log.d("error", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    private Context getContext() {
        return this;
    }

    @Override
    public void getResult(ArrayList<String> categories) {
        this.stringCategories = categories;
    }

    @SuppressLint("SetTextI18n")
    public void updateCounter(int chosen) {
        categoriesCounter.setText(chosen + "/15");
    }
}
