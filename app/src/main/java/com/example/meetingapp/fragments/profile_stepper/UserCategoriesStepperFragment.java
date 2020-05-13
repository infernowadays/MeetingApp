package com.example.meetingapp.fragments.profile_stepper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetingapp.R;
import com.example.meetingapp.adapters.CategoryChipsAdapter;
import com.example.meetingapp.api.RetrofitClient;
import com.example.meetingapp.models.MegaCategory;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserCategoriesStepperFragment extends Fragment implements BlockingStep {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    public static UserCategoriesStepperFragment newInstance() {
        return new UserCategoriesStepperFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_categories_stepper, container, false);
        ButterKnife.bind(this, view);

        getCategories();

        return view;
    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        callback.goToNextStep();
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {
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

    }

    @Override
    public void onError(@NonNull VerificationError error) {

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
                recyclerView.setAdapter(new CategoryChipsAdapter(getContext(), recyclerView, response.body()));

                RetrofitClient.needsHeader(true);
            }

            @Override
            public void onFailure(@NonNull Call<List<MegaCategory>> call, @NonNull Throwable t) {
                int a = 5;
            }
        });
    }
}
