package com.example.meetingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetingapp.DownloadImageTask;
import com.example.meetingapp.GetImageFromAsync;
import com.example.meetingapp.R;
import com.example.meetingapp.UserProfileManager;
import com.example.meetingapp.activities.EventActivity;
import com.example.meetingapp.activities.EventInfoActivity;
import com.example.meetingapp.api.RetrofitClient;
import com.example.meetingapp.models.Category;
import com.example.meetingapp.models.Event;
import com.example.meetingapp.models.EventRequest;
import com.example.meetingapp.utils.PreferenceUtils;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

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
    private List<Integer> eventsIds;

    public EventsAdapter(Context context, List<Event> events) {
        this.events = events;
        this.context = context;

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

        if (event.getCreator().getId() == UserProfileManager.getInstance().getMyProfile().getId())
            holder.buttonSendRequest.setEnabled(false);

        if (event.getCreator().getPhoto() != null) {
            holder.setImageProfile(event.getCreator().getPhoto().getPhoto());
        }

        int membersCount = event.getMembers().size();
        holder.textMembersCount.setText(String.valueOf(membersCount));

        TextView textCreatorName = holder.textCreatorName;
        String fullName = event.getCreator().getFirstName() + " " + event.getCreator().getLastName();
        textCreatorName.setText(fullName);

        TextView textEventDescription = holder.textEventDescription;
        textEventDescription.setText(event.getDescription());


        if (!ArrayUtils.contains(eventsIds.toArray(), events.get(position).getId())) {
            for (Category category : event.getCategories()) {
                Chip chip = (Chip) LayoutInflater.from(context).inflate(R.layout.category_item, holder.chipGroup, false);
                chip.setText(category.getName());
                chip.setCheckable(false);
                holder.chipGroup.addView(chip);
            }
        }
        eventsIds.add(events.get(position).getId());

        holder.itemView.setOnClickListener(v -> {
            if (event.getCreator().getId() == UserProfileManager.getInstance().getMyProfile().getId()) {
                Intent intent = new Intent(context, EventActivity.class);
                intent.putExtra("EXTRA_EVENT_ID", String.valueOf(event.getId()));
                context.startActivity(intent);
            } else {
                Intent intent = new Intent(context, EventInfoActivity.class);
                intent.putExtra("EXTRA_EVENT_ID", String.valueOf(event.getId()));
                context.startActivity(intent);
            }
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
                .sendRequest(new EventRequest(UserProfileManager.getInstance().getMyProfile(), toUser, event));

        call.enqueue(new Callback<EventRequest>() {
            @Override
            public void onResponse(@NonNull Call<EventRequest> call, @NonNull Response<EventRequest> response) {
                Log.d("response", response.message());
            }

            @Override
            public void onFailure(@NonNull Call<EventRequest> call, @NonNull Throwable t) {
                Log.d("failure", "request failed");
            }
        });
    }

    private void removeItemAfterRequest(int position) {
        events.remove(position);
        eventsIds = new ArrayList<>();

        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
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

    static class ViewHolder extends RecyclerView.ViewHolder implements GetImageFromAsync {
        @BindView(R.id.image_profile)
        ImageView imageProfile;

        @BindView(R.id.text_creator_name)
        TextView textCreatorName;

        @BindView(R.id.text_event_description)
        TextView textEventDescription;

        @BindView(R.id.text_members_count)
        TextView textMembersCount;

        @BindView(R.id.button_send_request)
        Button buttonSendRequest;

        @BindView(R.id.chip_group)
        ChipGroup chipGroup;

        Bitmap bitmap;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setImageProfile(String photoUrl) {
            new DownloadImageTask(ViewHolder.this).execute(photoUrl);
        }

        @Override
        public void getResult(Bitmap bitmap) {
            imageProfile.setImageBitmap(bitmap);
            this.bitmap = bitmap;
        }
    }
}
