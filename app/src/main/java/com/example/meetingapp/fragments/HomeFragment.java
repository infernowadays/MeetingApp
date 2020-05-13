package com.example.meetingapp.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.meetingapp.R;
import com.example.meetingapp.UserProfileManager;
import com.example.meetingapp.activities.SettingsActivity;
import com.example.meetingapp.api.RetrofitClient;
import com.example.meetingapp.models.UserProfile;
import com.example.meetingapp.utils.PreferenceUtils;
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

public class HomeFragment extends Fragment {

    @BindView(R.id.text_profile_name)
    TextView textProfileName;

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

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    private UserProfile userProfile;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());

        viewPagerAdapter.addFragment(new HomeEventsFragment("all"), "Все");
        viewPagerAdapter.addFragment(new HomeEventsFragment("creator"), "Мои");
        viewPagerAdapter.addFragment(new HomeEventsFragment("member"), "Иду");
        viewPagerAdapter.addFragment(new HomeEventsFragment("passed"), "Был(-а)");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        meProfile();

        return view;
    }


    @OnClick(R.id.button_settings)
    void openSettings() {
        Intent intent = new Intent(getActivity(), SettingsActivity.class);
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
            }

            @Override
            public void onFailure(@NonNull Call<UserProfile> call, @NonNull Throwable t) {

            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void showProfile() {
        textProfileName.setText(userProfile.getFirstName() + " " + userProfile.getLastName());

        if(userProfile.getDateOfBirth() != null){
            String age = getAgeFromBirthDateString(userProfile.getDateOfBirth());
                textYearsOld.setText(age);
                profileYearsOld.setVisibility(View.VISIBLE);
        }

        if (userProfile.getCity() != null) {
            textCity.setText(userProfile.getCity());
            profileCity.setVisibility(View.VISIBLE);
        }

        if (userProfile.getEducation() != null) {
            textEducation.setText(userProfile.getEducation());
            profileEducation.setVisibility(View.VISIBLE);
        }

        if (userProfile.getJob() != null) {
            textJob.setText(userProfile.getJob());
            profileJob.setVisibility(View.VISIBLE);
        }

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

    static class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;
        private int mCurrentPosition = -1;

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