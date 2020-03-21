package com.example.meetingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetingapp.R;
import com.example.meetingapp.activities.EventActivity;
import com.example.meetingapp.activities.MessageActivity;
import com.example.meetingapp.models.Event;

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

        TextView textViewEventId = holder.textViewEventId;
        textViewEventId.setText(String.valueOf(event.getId()));

        TextView textViewEventName = holder.textViewEventName;
        textViewEventName.setText(event.getName());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, EventActivity.class);
            intent.putExtra("eventId", String.valueOf(event.getId()));

            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewEventId;
        private TextView textViewEventName;

        ViewHolder(View itemView) {
            super(itemView);

            textViewEventId = itemView.findViewById(R.id.textViewEventId);
            textViewEventName = itemView.findViewById(R.id.textViewEventName);
        }
    }


}
