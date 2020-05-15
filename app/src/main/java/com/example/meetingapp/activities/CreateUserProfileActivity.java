package com.example.meetingapp.activities;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.meetingapp.IUserProfileManager;
import com.example.meetingapp.R;
import com.example.meetingapp.adapters.UserStepperAdapter;
import com.example.meetingapp.models.Category;
import com.stepstone.stepper.StepperLayout;

import java.util.ArrayList;
import java.util.List;

public class CreateUserProfileActivity extends AppCompatActivity implements IUserProfileManager {

    private static final String CURRENT_STEP_POSITION_KEY = "position";
    private static final String SEX = "sex";
    private static final String PHOTO_URL = "photoUrl";
    private static final String BIRTH_DATE = "birthDate";
    private static final String CITY = "city";
    private static final String EDUCATION = "education";
    private static final String JOB = "job";
    private static final String CATEGORIES = "categories";

    private StepperLayout stepperLayout;

    private String sex;
    private String photoUrl;
    private String birthDate;
    private String city;
    private String education;
    private String job;
    private ArrayList<String> categories;

    public static Context contextOfApplication;
    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step);

        contextOfApplication = this;

        stepperLayout = findViewById(R.id.stepperLayout);
        UserStepperAdapter userStepperAdapter = new UserStepperAdapter(getSupportFragmentManager(), this);
        stepperLayout.setAdapter(userStepperAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_STEP_POSITION_KEY, stepperLayout.getCurrentStepPosition());
        outState.putString(SEX, sex);
        outState.putString(PHOTO_URL, photoUrl);
        outState.putString(BIRTH_DATE, birthDate);
        outState.putString(CITY, city);
        outState.putString(EDUCATION, education);
        outState.putString(JOB, job);
        outState.putStringArrayList(CATEGORIES, categories);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        final int currentStepPosition = stepperLayout.getCurrentStepPosition();
        if (currentStepPosition > 0) {
            stepperLayout.onBackClicked();
        } else {
            finish();
        }
    }

    @Override
    public void saveSex(String sex) {
        this.sex = sex;
    }

    @Override
    public String getSex() {
        return sex;
    }

    @Override
    public void savePhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @Override
    public String getPhotoUrl() {
        return photoUrl;
    }

    @Override
    public void saveBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public String getBirthDate() {
        return birthDate;
    }

    @Override
    public void saveCity(String city) {
        this.city = city;
    }

    @Override
    public String getCity() {
        return city;
    }

    @Override
    public void saveEducation(String education) {
        this.education = education;
    }

    @Override
    public String getEducation() {
        return education;
    }

    @Override
    public void saveJob(String job) {
        this.job = job;
    }

    @Override
    public String getJob() {
        return job;
    }

    @Override
    public void saveCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    @Override
    public ArrayList<String> getCategories() {
        return categories;
    }
}
