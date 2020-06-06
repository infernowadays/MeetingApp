package com.example.meetingapp.fragments.ticket_stepper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.meetingapp.R;
import com.example.meetingapp.TicketManager;
import com.example.meetingapp.activities.TicketActivity;
import com.example.meetingapp.api.RetrofitClient;
import com.example.meetingapp.models.Category;
import com.example.meetingapp.models.Ticket;
import com.example.meetingapp.utils.PreferenceUtils;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TicketPublishStepperFragment extends Fragment implements BlockingStep {

    @BindView(R.id.text_date)
    TextView textDate;

    @BindView(R.id.text_name)
    TextView textName;

    @BindView(R.id.chip_group)
    ChipGroup chipGroup;

    @BindView(R.id.text_price)
    TextView textPrice;

    @BindView(R.id.text_description)
    TextView textDescription;

    private TicketManager ticketManager;
    private Context context;
    private Ticket ticket;
    private Ticket createdTicket;

    public static TicketPublishStepperFragment newInstance() {
        return new TicketPublishStepperFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ticket_publish_stepper, container, false);
        ButterKnife.bind(this, view);

        ticket = new Ticket();

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;

        if (context instanceof TicketManager) {
            ticketManager = (TicketManager) context;
        } else {
            throw new IllegalStateException("Activity must implement ticketManager interface!");
        }
    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {
        publishTicket();
    }

    private void openCreatedTicket() {
        Intent intent = new Intent(getActivity(), TicketActivity.class);
        intent.putExtra("EXTRA_TICKET_ID", String.valueOf(createdTicket.getId()));

        finishActivity();
        startActivity(intent);
    }

    private void finishActivity() {
        requireActivity().finish();
    }

    private void publishTicket() {
        Call<Ticket> call = RetrofitClient
                .getInstance(PreferenceUtils.getToken(requireContext()))
                .getApi()
                .createTicket(ticket);

        call.enqueue(new Callback<Ticket>() {
            @Override
            public void onResponse(@NonNull Call<Ticket> call, @NonNull Response<Ticket> response) {
                createdTicket = response.body();
                if (createdTicket != null) {
                    Toast.makeText(getActivity(), "Билет опубликован!", Toast.LENGTH_SHORT).show();
                    openCreatedTicket();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Ticket> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), "Что-то случилось :(", Toast.LENGTH_SHORT).show();
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
        textDate.setText(ticketManager.getDate());
        ticket.setDate(ticketManager.getDate());

        textName.setText(ticketManager.getName());
        ticket.setName(ticketManager.getName());

        textPrice.setText(String.valueOf(ticketManager.getPrice()));
        ticket.setPrice(ticketManager.getPrice());

        if (ticketManager.getDescription() != null) {
            textDescription.setText(ticketManager.getDescription());
            textDescription.setVisibility(View.VISIBLE);

            ticket.setDescription(ticketManager.getDescription());
        }

        List<Category> categories = new ArrayList<>();
        chipGroup.removeAllViews();
        for (String category : ticketManager.getCategories()) {
            Chip chip = (Chip) LayoutInflater.from(getContext()).inflate(R.layout.category_item, chipGroup, false);
            chip.setText(category);
            chip.setCheckable(false);
            chipGroup.addView(chip);

            categories.add(new Category(category));
        }

        ticket.setCategories(categories);

        ticket.setAddress("address");
    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }
}
