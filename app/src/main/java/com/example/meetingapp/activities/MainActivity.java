package com.example.meetingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.meetingapp.NotificationListener;
import com.example.meetingapp.R;
import com.example.meetingapp.UserProfileManager;
import com.example.meetingapp.fragments.EventsFragment;
import com.example.meetingapp.fragments.MessagesFragment;
import com.example.meetingapp.fragments.TicketsFragment;
import com.example.meetingapp.ui.home.HomeFragment;
import com.example.meetingapp.utils.PreferenceUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NotificationListener {

    final Fragment homeFragment = new HomeFragment();
    final Fragment eventsFragment = new EventsFragment();
    final Fragment ticketsFragment = new TicketsFragment();
    final Fragment messagesFragment = new MessagesFragment();
    final FragmentManager fm = getSupportFragmentManager();

    private int notSeenNotifications = 0;
    private Fragment active = homeFragment;

    private BottomNavigationView navigation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                fm.beginTransaction().hide(active).show(homeFragment).commit();
                active = homeFragment;
                return true;
            case R.id.navigation_events:
                fm.beginTransaction().hide(active).show(eventsFragment).commit();
                active = eventsFragment;
                return true;
            case R.id.navigation_tickets:
                fm.beginTransaction().hide(active).show(ticketsFragment).commit();
                active = ticketsFragment;
                return true;
            case R.id.navigation_messages:
                fm.beginTransaction().hide(active).show(messagesFragment).commit();
                active = messagesFragment;

                return true;
        }
        return false;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fm.beginTransaction().add(R.id.main_container, messagesFragment, "4").hide(messagesFragment).commit();
        fm.beginTransaction().add(R.id.main_container, eventsFragment, "3").hide(eventsFragment).commit();
        fm.beginTransaction().add(R.id.main_container, ticketsFragment, "2").hide(ticketsFragment).commit();
        fm.beginTransaction().add(R.id.main_container, homeFragment, "1").commit();


//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Request");
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                navigation.getOrCreateBadge(R.id.navigation_messages);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_nav_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void addNotificationBadge(int number) {
        navigation.getOrCreateBadge(R.id.navigation_messages).setNumber(number);
    }

    @Override
    public void removeNotificationBadge() {
        navigation.removeBadge(R.id.navigation_messages);
    }
}
