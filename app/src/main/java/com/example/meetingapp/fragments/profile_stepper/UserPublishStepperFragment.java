package com.example.meetingapp.fragments.profile_stepper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.meetingapp.IUserProfileManager;
import com.example.meetingapp.R;
import com.example.meetingapp.activities.MainActivity;
import com.example.meetingapp.api.RetrofitClient;
import com.example.meetingapp.models.Category;
import com.example.meetingapp.models.UserProfile;
import com.example.meetingapp.utils.PreferenceUtils;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserPublishStepperFragment extends Fragment implements BlockingStep {


    @BindView(R.id.text_birth_date)
    TextView textBirthDate;

    @BindView(R.id.image_profile)
    CircleImageView imageProfile;

    @BindView(R.id.text_sex)
    TextView textSex;

    @BindView(R.id.chip_group)
    ChipGroup chipGroup;

    @BindView(R.id.text_city)
    TextView textCity;

    @BindView(R.id.text_education)
    TextView textEducation;

    @BindView(R.id.text_job)
    TextView textJob;

    @BindView(R.id.header_h4)
    TextView headerH4;

    private IUserProfileManager iUserProfileManager;
    private Bitmap bitmap;
    private UserProfile userProfile;

    public static UserPublishStepperFragment newInstance() {
        return new UserPublishStepperFragment();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_publish_stepper, container, false);
        ButterKnife.bind(this, view);

        int unicode = 0x1F929;
        headerH4.setText("Ура! Создание профиля закончено " + new String(Character.toChars(unicode)));

        userProfile = new UserProfile();

        return view;
    }

    @Override
    public void onAttach(@androidx.annotation.NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IUserProfileManager) {
            iUserProfileManager = (IUserProfileManager) context;
        } else {
            throw new IllegalStateException("Activity must implement EventManager interface!");
        }
    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {

    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {
        createUserProfile();
    }

    private void createUserProfile() {
        RetrofitClient.needsHeader(true);

        Call<UserProfile> call = RetrofitClient
                .getInstance(PreferenceUtils.getToken(requireContext()))
                .getApi()
                .updateProfile(userProfile);

        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(@NonNull Call<UserProfile> call, @NonNull Response<UserProfile> response) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                requireContext().startActivity(intent);
                ((Activity) requireContext()).finish();

                PreferenceUtils.saveFilled(true, getContext());
            }

            @Override
            public void onFailure(@NonNull Call<UserProfile> call, @NonNull Throwable t) {
                Log.d("error", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {
        if (bitmap == null) {
            byte[] byteArray = iUserProfileManager.getPhoto();
            bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        }
        imageProfile.setImageBitmap(bitmap);

        textBirthDate.setText(iUserProfileManager.getBirthDate());
        userProfile.setDateOfBirth(iUserProfileManager.getBirthDate());

        textSex.setText(iUserProfileManager.getSex());
        userProfile.setSex(iUserProfileManager.getSex());

        if (iUserProfileManager.getCity() != null) {
            textCity.setText(iUserProfileManager.getCity());
            textCity.setVisibility(View.VISIBLE);

            userProfile.setCity(iUserProfileManager.getCity());
        }

        if (iUserProfileManager.getEducation() != null) {
            textEducation.setText(iUserProfileManager.getEducation());
            textEducation.setVisibility(View.VISIBLE);

            userProfile.setEducation(iUserProfileManager.getEducation());
        }

        if (iUserProfileManager.getJob() != null) {
            textJob.setText(iUserProfileManager.getJob());
            textJob.setVisibility(View.VISIBLE);

            userProfile.setJob(iUserProfileManager.getJob());
        }

        List<Category> categories = new ArrayList<>();
        chipGroup.removeAllViews();
        for (String category : iUserProfileManager.getCategories()) {
            Chip chip = (Chip) LayoutInflater.from(getContext()).inflate(R.layout.category_item, chipGroup, false);
            chip.setText(category);
            chip.setCheckable(false);
            chipGroup.addView(chip);

            categories.add(new Category(category));
        }

        userProfile.setCategories(categories);
        userProfile.setFilled(true);
    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }
}
