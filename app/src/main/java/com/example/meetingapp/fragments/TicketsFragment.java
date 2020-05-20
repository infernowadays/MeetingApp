package com.example.meetingapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.meetingapp.R;
import com.example.meetingapp.activities.CreateTicketActivity;
import com.example.meetingapp.adapters.TicketsAdapter;
import com.example.meetingapp.api.RetrofitClient;
import com.example.meetingapp.models.Ticket;
import com.example.meetingapp.utils.PreferenceUtils;

import java.util.List;

import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TicketsFragment extends ContentFragment {

    @OnClick(R.id.floating_action_button)
    void createTicket() {
        Intent intent = new Intent(getActivity(), CreateTicketActivity.class);
        intent.putExtra("action", "create");

        startActivity(intent);
    }

    @Override
    public void openFilterDialog() {

    }

    @Override
    public void loadContent(List<String> categories) {
        Call<List<Ticket>> call = RetrofitClient
                .getInstance(PreferenceUtils.getToken(requireContext()))
                .getApi()
                .getTickets(categories, null);

        call.enqueue(new Callback<List<Ticket>>() {
            @Override
            public void onResponse(@NonNull Call<List<Ticket>> call, @NonNull Response<List<Ticket>> response) {
                List<Ticket> tickets = response.body();
                if (tickets != null)
                    recyclerView.setAdapter(new TicketsAdapter(getContext(), tickets));
            }

            @Override
            public void onFailure(@NonNull Call<List<Ticket>> call, @NonNull Throwable t) {

            }
        });
    }

    @Override
    public String getContentType() {
        return "TICKETS";
    }
}