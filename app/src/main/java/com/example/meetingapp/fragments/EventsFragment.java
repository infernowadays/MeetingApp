package com.example.meetingapp.fragments;

import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.meetingapp.R;
import com.example.meetingapp.UserProfileManager;
import com.example.meetingapp.activities.CreateEventActivity;
import com.example.meetingapp.adapters.EventsAdapter;
import com.example.meetingapp.api.RetrofitClient;
import com.example.meetingapp.models.Event;
import com.example.meetingapp.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EventsFragment extends ContentFragment {

    @OnClick(R.id.floating_action_button)
    void createEvent() {
        if (!UserProfileManager.getInstance().getMyProfile().getConfirmed())
            Toast.makeText(getContext(), "Чтобы создавать события, подтвердите аккаунт в личном кабинете", Toast.LENGTH_SHORT).show();
        else {
            Intent intent = new Intent(getActivity(), CreateEventActivity.class);
            intent.putExtra("action", "create");

            startActivity(intent);
        }
    }

    @Override
    public void openFilterDialog() {
        EventsFilterDialog dialog = EventsFilterDialog.newInstance();
        dialog.setTargetFragment(EventsFragment.this, REQUEST_CODE);
        dialog.setCallback(this::loadContent);
        dialog.show(requireActivity().getSupportFragmentManager(), "tag");
    }

    @Override
    public void loadContent(List<String> categories) {
        Call<List<Event>> call = RetrofitClient
                .getInstance(PreferenceUtils.getToken(requireContext()))
                .getApi()
                .getEvents(categories, null);

        call.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(@NonNull Call<List<Event>> call, @NonNull Response<List<Event>> response) {
                List<Event> events = response.body();
                if (events == null)
                    events = new ArrayList<>();

                filterByDistance(events);
                recyclerView.setAdapter(new EventsAdapter(getContext(), events));
            }

            @Override
            public void onFailure(@NonNull Call<List<Event>> call, @NonNull Throwable t) {

            }
        });
    }

    private void filterByDistance(List<Event> events) {

        double maxDistance = 150;

        if (currentLocation != null) {
            for (Iterator<Event> iterator = events.iterator(); iterator.hasNext(); ) {
                Event event = iterator.next();
                double eventLatitude = event.getGeoPoint().getLatitude();
                double eventLongitude = event.getGeoPoint().getLongitude();
                if (distance(eventLatitude, eventLongitude, currentLocation.getLatitude(), currentLocation.getLongitude()) >= maxDistance) {
                    iterator.remove();
                }
            }
        }
    }

    @Override
    public String getContentType() {
        return "СОБЫТИЯ";
    }
}