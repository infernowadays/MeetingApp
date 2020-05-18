package com.example.meetingapp.fragments.ticket_stepper;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.meetingapp.R;
import com.example.meetingapp.TicketManager;
import com.example.meetingapp.models.Category;
import com.example.meetingapp.models.Ticket;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
        createTicket();
    }

    private void createTicket() {
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
    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }
}
