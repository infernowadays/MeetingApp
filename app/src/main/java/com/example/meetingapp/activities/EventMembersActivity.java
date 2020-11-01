package com.example.meetingapp.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetingapp.R;
import com.example.meetingapp.adapters.MembersAdapter;
import com.example.meetingapp.models.UserProfile;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventMembersActivity extends AppCompatActivity {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_members);
        ButterKnife.bind(this);

        List<UserProfile> members = getIntent().getParcelableArrayListExtra("EXTRA_MEMBERS");
        MembersAdapter membersAdapter = new MembersAdapter(this, members);

        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(membersAdapter);
    }
}