package com.example.meetingapp.fragments;

import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.meetingapp.R;
import com.example.meetingapp.services.UserProfileManager;
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
        if (!UserProfileManager.getInstance().getMyProfile().getConfirmed())
            Toast.makeText(getContext(), "Чтобы публиковать билеты, подтвердите аккаунт в личном кабинете", Toast.LENGTH_SHORT).show();
        else {
            Intent intent = new Intent(getActivity(), CreateTicketActivity.class);
            intent.putExtra("action", "create");

            startActivity(intent);
        }
    }

    @Override
    public void openFilterDialog() {
//        TicketsFilterDialog dialog = TicketsFilterDialog.newInstance();
//        dialog.setTargetFragment(TicketsFragment.this, REQUEST_CODE);
//        dialog.setCallback(this::loadContent);
//        dialog.show(requireActivity().getSupportFragmentManager(), "tag");
    }

    @Override
    public void openSearchDialog() {

    }

    @Override
    public void loadContent(List<String> categories, List<String> sex, String fromAge, String toAge, String latitude, String longitude, String distance, String text, boolean renew) {
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
        return "БИЛЕТЫ";
    }
}