package com.example.meetingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetingapp.R;
import com.example.meetingapp.activities.EventActivity;
import com.example.meetingapp.models.Category;
import com.example.meetingapp.models.Event;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

    private List<Event> mEvents;
    private Context mContext;

    public EventsAdapter(Context mContext, List<Event> events) {
        this.mEvents = events;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.event_item, parent, false);
        return new EventsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Event event = mEvents.get(position);

        TextView event_name = holder.name;
        event_name.setText(event.getName());

        TextView event_description = holder.description;
        event_description.setText(event.getDescription());

        for (Category category : event.getCategories()) {
            Chip chip = (Chip) LayoutInflater.from(mContext).inflate(R.layout.category_item, holder.chipGroup, false);
            chip.setText(category.getName());
            holder.chipGroup.addView(chip);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, EventActivity.class);
            intent.putExtra("eventId", String.valueOf(event.getId()));

            mContext.startActivity(intent);
        });

        holder.sendRequestButton.setOnClickListener(v -> {
            sendRequest(event.getId(), "FfO8eOMug1PMondcKrfALb3Piu03");
        });
    }

    private void sendRequest(int event_id, String creator_id) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", firebaseUser.getUid());
        hashMap.put("creator_id", creator_id);
        hashMap.put("event_id", event_id);
        hashMap.put("decision", "NO_ANSWER");

        reference.child("Request").push().setValue(hashMap);
        Toast.makeText(mContext, "Запрос отправлен!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView description;
        private ChipGroup chipGroup;
        private Button sendRequestButton;

        ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.event_name);
            description = itemView.findViewById(R.id.event_description);
            chipGroup = itemView.findViewById(R.id.chip_group);

            sendRequestButton = itemView.findViewById(R.id.go_btn);
        }
    }
}
