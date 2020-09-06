package com.example.meetingapp.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;

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

    private EventsAdapter eventsAdapter;

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
    public void openSearchDialog() {
        MenuItem menuItem = myMenu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();

        ImageView searchClose = (ImageView) searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
//        searchClose.setColorFilter(R.color.ms_white);
        searchClose.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                eventsAdapter.getFilter().filter(newText);
                return false;
            }
        });
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

//                filterByDistance(events);
                eventsAdapter = new EventsAdapter(getContext(), events);
                recyclerView.setAdapter(eventsAdapter);
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