package com.example.meetingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetingapp.R;
import com.example.meetingapp.UserProfileManager;
import com.example.meetingapp.activities.EventActivity;
import com.example.meetingapp.api.FirebaseClient;
import com.example.meetingapp.api.RetrofitClient;
import com.example.meetingapp.models.Category;
import com.example.meetingapp.models.Event;
import com.example.meetingapp.models.EventRequest;
import com.example.meetingapp.utils.PreferenceUtils;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

    private List<Event> events;
    private Context context;
    private FirebaseClient firebaseClient;
    private List<Integer> eventsIds;

    public EventsAdapter(Context context, List<Event> events) {
        this.events = events;
        this.context = context;
        firebaseClient = new FirebaseClient(context);

        eventsIds = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.event_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Event event = events.get(position);

        TextView textCreatorName = holder.textCreatorName;
        textCreatorName.setText(event.getCreator().getUsername());

        TextView textEventDescription = holder.textEventDescription;
        textEventDescription .setText(event.getDescription());

        if(!ArrayUtils.contains(eventsIds.toArray(), events.get(position).getId())){
            for (Category category : event.getCategories()) {
                Chip chip = (Chip) LayoutInflater.from(context).inflate(R.layout.category_item, holder.chipGroup, false);
                chip.setText(category.getName());
                holder.chipGroup.addView(chip);
            }
        }
        eventsIds.add(events.get(position).getId());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EventActivity.class);
            intent.putExtra("EXTRA_EVENT_ID", String.valueOf(event.getId()));

            context.startActivity(intent);
        });

        holder.buttonSendRequest.setOnClickListener(v -> {
            sendRequest(String.valueOf(event.getCreator().getId()), event.getId());
            removeItemAfterRequest(position);
        });
    }

    private void sendRequest(String toUser, long event) {
        Call<EventRequest> call = RetrofitClient
                .getInstance(PreferenceUtils.getToken(Objects.requireNonNull(context)))
                .getApi()
                .sendRequest(new EventRequest(String.valueOf(UserProfileManager.getInstance().getMyProfile().getId()), toUser, event));

        call.enqueue(new Callback<EventRequest>() {
            @Override
            public void onResponse(@NonNull Call<EventRequest> call, @NonNull Response<EventRequest> response) {

            }

            @Override
            public void onFailure(@NonNull Call<EventRequest> call, @NonNull Throwable t) {

            }
        });
    }

    private void removeItemAfterRequest(int position) {
        events.remove(position);
        eventsIds = new ArrayList<>();

        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
//        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return events.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_creator_name)
        TextView textCreatorName;

        @BindView(R.id.text_event_description)
        TextView textEventDescription;

        @BindView(R.id.button_send_request)
        Button buttonSendRequest;

        @BindView(R.id.chip_group)
        ChipGroup chipGroup;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
