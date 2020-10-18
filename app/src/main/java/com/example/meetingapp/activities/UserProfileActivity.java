package com.example.meetingapp.activities;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.meetingapp.utils.images.DownloadImageTask;
import com.example.meetingapp.interfaces.GetImageFromAsync;
import com.example.meetingapp.R;
import com.example.meetingapp.services.UserProfileManager;
import com.example.meetingapp.api.RetrofitClient;
import com.example.meetingapp.fragments.HomeEventsFragment;
import com.example.meetingapp.models.Category;
import com.example.meetingapp.models.UserProfile;
import com.example.meetingapp.utils.PreferenceUtils;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.tabs.TabLayout;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileActivity extends AppCompatActivity implements GetImageFromAsync {

    @BindView(R.id.image_profile)
    ImageView imageProfile;


    @BindView(R.id.text_first_name)
    TextView textFirstName;

    @BindView(R.id.text_last_name)
    TextView textLastName;

    @BindView(R.id.text_years_old)
    TextView textYearsOld;

    @BindView(R.id.profile_years_old)
    LinearLayout profileYearsOld;

    @BindView(R.id.text_city)
    TextView textCity;

    @BindView(R.id.profile_city)
    LinearLayout profileCity;

    @BindView(R.id.text_education)
    TextView textEducation;

    @BindView(R.id.profile_education)
    LinearLayout profileEducation;

    @BindView(R.id.text_job)
    TextView textJob;

    @BindView(R.id.profile_job)
    LinearLayout profileJob;

    @BindView(R.id.chip_group)
    ChipGroup chipGroup;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.text)
    TextView textView;

    @BindView(R.id.content_buttons)
    LinearLayout contentButtons;

    @BindView(R.id.button_write_message)
    MaterialButton buttonWriteMessage;

    @BindView(R.id.button_edit_categories)
    ImageButton buttonEditCategories;

    @BindView(R.id.button_edit_info)
    ImageButton buttonEditInfo;

    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;

    private UserProfile userProfile;
    private boolean prevSwipeState = true;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragment(new HomeEventsFragment("creator"), "БИЛЕТЫ");
        viewPagerAdapter.addFragment(new HomeEventsFragment("passed"), "СОБЫТИЯ");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        loadProfile();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float v, int i1) {
                swipeRefreshLayout.setEnabled(false);
            }

            @Override
            public void onPageSelected(int position) {
                swipeRefreshLayout.setEnabled(prevSwipeState);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                swipeRefreshLayout.setEnabled(state == ViewPager.SCROLL_STATE_DRAGGING);
                if (state == ViewPager.SCROLL_STATE_IDLE)
                    swipeRefreshLayout.setEnabled(prevSwipeState);

            }
        });

        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (verticalOffset == 0) {
                swipeRefreshLayout.setEnabled(true);
                prevSwipeState = true;
            } else {
                swipeRefreshLayout.setRefreshing(false);
                swipeRefreshLayout.setEnabled(false);
                prevSwipeState = false;
            }
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            loadProfile();

            swipeRefreshLayout.setRefreshing(false);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
//        setContent();
        if (bitmap != null)
            imageProfile.setImageBitmap(bitmap);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_options_menu, menu);
        MenuItem item = menu.getItem(0);
        SpannableString s = new SpannableString("Пожаловаться");
        s.setSpan(new ForegroundColorSpan(Color.RED), 0, s.length(), 0);
        item.setTitle(s);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_complain:
                // do your code
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void loadProfile() {
        String pk = getIntent().getStringExtra("EXTRA_USER_PROFILE_ID");


        Call<UserProfile> call = RetrofitClient
                .getInstance(PreferenceUtils.getToken(this))
                .getApi()
                .getUserProfile(pk);

        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(@NonNull Call<UserProfile> call, @NonNull Response<UserProfile> response) {
                if (response.body() != null) {
                    userProfile = response.body();
                    showProfile();
                    HideIfNotCurrentUserProfile();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserProfile> call, @NonNull Throwable t) {

            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void showProfile() {
        textFirstName.setText(userProfile.getFirstName());
        textLastName.setText(userProfile.getLastName());

        if (userProfile.getPhoto().getPhoto() != null) {
            new DownloadImageTask(UserProfileActivity.this).execute(userProfile.getPhoto().getPhoto());
        }

        if (!userProfile.getDateOfBirth().equals("")) {
            String age = getAgeFromBirthDateString(userProfile.getDateOfBirth());
            age = age + " " + getStringYear(Integer.parseInt(age));
            textYearsOld.setText(age);
            profileYearsOld.setVisibility(View.VISIBLE);
        }

        if (!userProfile.getCity().equals("")) {
            textCity.setText(userProfile.getCity());
            profileCity.setVisibility(View.VISIBLE);
        }

        if (!userProfile.getEducation().equals("")) {
            textEducation.setText(userProfile.getEducation());
            profileEducation.setVisibility(View.VISIBLE);
        }

        if (userProfile.getJob().equals("")) {
            textJob.setText(userProfile.getJob());
            profileJob.setVisibility(View.VISIBLE);
        }

        for (Category category : userProfile.getCategories()) {
            Chip chip = (Chip) LayoutInflater.from(this).inflate(R.layout.category_item, chipGroup, false);
            chip.setText(category.getName());
            chip.setCheckable(false);
            chipGroup.addView(chip);
        }
    }

    private String getStringYear(int age) {
        int year = age % 10;
        String stringYears = "";

        switch (year) {
            case 1:
                stringYears = "год";
                break;
            case 2:
            case 3:
            case 4:
                stringYears = "года";
                break;
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 0:
                stringYears = "лет";
                break;

        }

        return stringYears;
    }

    private String getAgeFromBirthDateString(String birthDateString) {
        LocalDate currentDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentDate = LocalDate.now();
            LocalDate birthDate = LocalDate.parse(birthDateString);

            return String.valueOf(Period.between(birthDate, currentDate).getYears());
        }
        return "";
    }

    private void HideIfNotCurrentUserProfile() {
        if (userProfile.getId() == UserProfileManager.getInstance().getMyProfile().getId()) {
            contentButtons.setVisibility(View.VISIBLE);
            buttonEditCategories.setVisibility(View.VISIBLE);
            buttonEditInfo.setVisibility(View.VISIBLE);
            buttonWriteMessage.setVisibility(View.GONE);
        }
    }

    @Override
    public void getResult(Bitmap bitmap) {
        if (bitmap != null) {
            imageProfile.setImageBitmap(bitmap);
            this.bitmap = bitmap;
        }
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {

            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
}
