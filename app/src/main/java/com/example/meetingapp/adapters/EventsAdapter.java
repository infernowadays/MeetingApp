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
import com.example.meetingapp.activities.EventActivity;
import com.example.meetingapp.api.FirebaseClient;
import com.example.meetingapp.api.RetrofitClient;
import com.example.meetingapp.models.Category;
import com.example.meetingapp.models.Event;
import com.example.meetingapp.models.EventRequest;
import com.example.meetingapp.utils.PreferenceUtils;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;


public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

    private List<Event> events;
    private Context context;
    private FirebaseClient firebaseClient;

    public EventsAdapter(Context context, List<Event> events) {
        this.events = events;
        this.context = context;
        firebaseClient = new FirebaseClient(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.event_item, parent, false);
        return new EventsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Event event = events.get(position);

        TextView textCreatorName = holder.textCreatorName;
        textCreatorName.setText(event.getName());

        TextView textEventDescription = holder.textEventDescription;
        textEventDescription .setText(event.getDescription());

        for (Category category : event.getCategories()) {
            Chip chip = (Chip) LayoutInflater.from(context).inflate(R.layout.category_item, holder.chipGroup, false);
            chip.setText(category.getName());
            holder.chipGroup.addView(chip);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EventActivity.class);
            intent.putExtra("EXTRA_EVENT_ID", String.valueOf(event.getId()));

            context.startActivity(intent);
        });

        holder.buttonSendRequest.setOnClickListener(v -> {
            sendRequest(event.getCreator().getFirebaseUid(), event.getId());
            removeItemAfterRequest(position);
        });
    }

    private void sendRequest(String toUser, long event) {
        Call<EventRequest> call = RetrofitClient
                .getInstance(PreferenceUtils.getToken(Objects.requireNonNull(context)))
                .getApi()
                .sendRequest(new EventRequest(firebaseClient.getUid(), toUser, event));

        call.enqueue(new Callback<EventRequest>() {
            @Override
            public void onResponse(Call<EventRequest> call, retrofit2.Response<EventRequest> response) {
                firebaseClient.sendRequest(toUser, event);
            }

            @Override
            public void onFailure(Call<EventRequest> call, Throwable t) {

            }
        });
    }

    private void removeItemAfterRequest(int position) {
        events.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }


    @Override
    public int getItemCount() {
        return events.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
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
