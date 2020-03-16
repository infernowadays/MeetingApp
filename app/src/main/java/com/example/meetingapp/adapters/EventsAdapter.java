package com.example.meetingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetingapp.R;
import com.example.meetingapp.models.Event;

import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

    private List<Event> mEvents;

    public EventsAdapter(List<Event> events) {
        mEvents = events;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.event_item, parent, false);

        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = mEvents.get(position);

        TextView textViewEventId = holder.textViewEventId;
        textViewEventId.setText(String.valueOf(event.getId()));

        TextView textViewEventName = holder.textViewEventName;
        textViewEventName.setText(event.getName());
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewEventId;
        public TextView textViewEventName;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewEventId = itemView.findViewById(R.id.textViewEventId);
            textViewEventName = itemView.findViewById(R.id.textViewEventName);
        }
    }


}
