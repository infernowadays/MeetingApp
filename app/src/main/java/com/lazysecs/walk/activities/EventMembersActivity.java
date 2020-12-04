package com.lazysecs.walk.activities;

import android.os.Bundle;
import android.view.KeyEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lazysecs.walk.R;
import com.lazysecs.walk.adapters.MembersAdapter;
import com.lazysecs.walk.models.UserProfile;

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
        int eventId = getIntent().getIntExtra("EXTRA_EVENT_ID", 0);
        MembersAdapter membersAdapter = new MembersAdapter(this, members, eventId);

        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(membersAdapter);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}