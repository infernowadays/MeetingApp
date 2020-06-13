package com.example.meetingapp.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.meetingapp.DownloadImageTask;
import com.example.meetingapp.GetImageFromAsync;
import com.example.meetingapp.R;
import com.example.meetingapp.UserProfileManager;
import com.example.meetingapp.activities.ConfirmCodeActivity;
import com.example.meetingapp.activities.EditUserProfileActivity;
import com.example.meetingapp.activities.EditUserProfileCategoriesActivity;
import com.example.meetingapp.activities.SettingsActivity;
import com.example.meetingapp.activities.UserEventsActivity;
import com.example.meetingapp.activities.UserTicketsActivity;
import com.example.meetingapp.api.RetrofitClient;
import com.example.meetingapp.models.Category;
import com.example.meetingapp.models.UserProfile;
import com.example.meetingapp.utils.PreferenceUtils;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.tabs.TabLayout;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements GetImageFromAsync {

    @BindView(R.id.image_profile)
    ImageView imageProfile;

    @BindView(R.id.button_confirm)
    ImageView buttonConfirm;

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

    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.home_content)
    CoordinatorLayout homeContent;

    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;

    @BindView(R.id.progressbar_downloading)
    ProgressBar progressBar;

    @BindView(R.id.progressbar_layout)
    RelativeLayout layoutProgressBar;

    @BindView(R.id.layout_content)
    NestedScrollView nestedScrollView;

    private UserProfile userProfile;
    private Bitmap bitmap;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);


        layoutProgressBar.setVisibility(View.VISIBLE);
        appBarLayout.setVisibility(View.GONE);
        nestedScrollView.setVisibility(View.GONE);

        setContent();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float v, int i1) {
                swipeRefreshLayout.setEnabled(false);
            }

            @Override
            public void onPageSelected(int position) {
                swipeRefreshLayout.setEnabled(false);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                swipeRefreshLayout.setEnabled(state == ViewPager.SCROLL_STATE_DRAGGING);
            }
        });

        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (verticalOffset == 0) {
                swipeRefreshLayout.setEnabled(true);
            } else {
                swipeRefreshLayout.setRefreshing(false);
                swipeRefreshLayout.setEnabled(false);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            setContent();
            meProfile();

            swipeRefreshLayout.setRefreshing(false);
        });

        meProfile();

        return view;
    }


    private void setContent() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());

        viewPagerAdapter.addFragment(new HomeEventsFragment("creator"), "События");
        viewPagerAdapter.addFragment(new HomeTicketsFragment("creator"), "Билеты");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @OnClick(R.id.button_edit_categories)
    void editCategories() {
        Intent intent = new Intent(getActivity(), EditUserProfileCategoriesActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.button_edit_user_profile)
    void editUserProfile() {
        Intent intent = new Intent(getActivity(), EditUserProfileActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.button_confirm)
    void confirmAccount() {
        Intent intent = new Intent(getActivity(), ConfirmCodeActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.button_settings)
    void openSettings() {
        Intent intent = new Intent(getActivity(), SettingsActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.button_user_events)
    void openUserEvents() {
        Intent intent = new Intent(getActivity(), UserEventsActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.button_user_tickets)
    void openUserTickets() {
        Intent intent = new Intent(getActivity(), UserTicketsActivity.class);
        startActivity(intent);
    }

    private void meProfile() {
        Call<UserProfile> call = RetrofitClient
                .getInstance(PreferenceUtils.getToken(requireContext()))
                .getApi()
                .meProfile();

        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(@NonNull Call<UserProfile> call, @NonNull Response<UserProfile> response) {
                if (response.body() != null) {
                    userProfile = response.body();
                    UserProfileManager.getInstance().initialize(userProfile);
                    showProfile();
                }

                layoutProgressBar.setVisibility(View.GONE);
                appBarLayout.setVisibility(View.VISIBLE);
                nestedScrollView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(@NonNull Call<UserProfile> call, @NonNull Throwable t) {
                layoutProgressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setContent();
        if (bitmap != null)
            imageProfile.setImageBitmap(bitmap);
    }

    @SuppressLint("SetTextI18n")
    private void showProfile() {
        if (!userProfile.getConfirmed())
            buttonConfirm.setVisibility(View.VISIBLE);

        if (userProfile.getPhoto().getPhoto() != null) {
            new DownloadImageTask(HomeFragment.this).execute(userProfile.getPhoto().getPhoto());
        }

        textFirstName.setText(userProfile.getFirstName());
        textLastName.setText(userProfile.getLastName());

        if (!userProfile.getDateOfBirth().equals("")) {
            String age = getAgeFromBirthDateString(userProfile.getDateOfBirth());
            age = age + " " + getStringYear(Integer.parseInt(age));
            textYearsOld.setText(age);
            profileYearsOld.setVisibility(View.VISIBLE);
        }

        if (!userProfile.getCity().equals("")) {
            textCity.setText(userProfile.getCity());
            profileCity.setVisibility(View.VISIBLE);
        } else {
            profileCity.setVisibility(View.GONE);
        }

        if (!userProfile.getEducation().equals("")) {
            textEducation.setText(userProfile.getEducation());
            profileEducation.setVisibility(View.VISIBLE);
        } else {
            profileEducation.setVisibility(View.GONE);
        }

        if (!userProfile.getJob().equals("")) {
            textJob.setText(userProfile.getJob());
            profileJob.setVisibility(View.VISIBLE);
        } else {
            profileJob.setVisibility(View.GONE);
        }

        chipGroup.removeAllViews();
        for (Category category : userProfile.getCategories()) {
            Chip chip = (Chip) LayoutInflater.from(getContext()).inflate(R.layout.category_item, chipGroup, false);
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