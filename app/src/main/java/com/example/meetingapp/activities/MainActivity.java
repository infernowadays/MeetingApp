package com.example.meetingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.meetingapp.NotificationListener;
import com.example.meetingapp.R;
import com.example.meetingapp.fragments.BottomSheetFragment;
import com.example.meetingapp.fragments.EventsFragment;
import com.example.meetingapp.fragments.HomeFragment;
import com.example.meetingapp.fragments.MessagesFragment;
import com.example.meetingapp.fragments.TicketsFragment;
import com.example.meetingapp.services.WebSocketListenerService;
import com.example.meetingapp.utils.PreferenceUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NotificationListener, BottomSheetFragment.ItemClickListener {

    final Fragment homeFragment = new HomeFragment();
    final Fragment eventsFragment = new EventsFragment();
    final Fragment ticketsFragment = new TicketsFragment();
    final Fragment messagesFragment = new MessagesFragment();
    final FragmentManager fm = getSupportFragmentManager();
    private final String EVENTS = "EVENTS";
    private final String TICKETS = "TICKETS";
    private final int eventIcon = R.drawable.ic_events;
    private final int ticketIcon = R.drawable.ic_tickets;

    private int notSeenNotifications = 0;
    private Fragment active = homeFragment;
    private String content;

    private BottomNavigationView navigation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                fm.beginTransaction().hide(active).show(homeFragment).commit();
                active = homeFragment;
                return true;
            case R.id.navigation_content:
                if (content.equals(EVENTS)) {
                    fm.beginTransaction().hide(active).show(eventsFragment).commit();
                    active = eventsFragment;
                } else if (content.equals(TICKETS)) {
                    fm.beginTransaction().hide(active).show(ticketsFragment).commit();
                    active = ticketsFragment;
                }
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
        ButterKnife.bind(this);

        navigation = findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        content = PreferenceUtils.getContentType(this);
        if (content.equals(TICKETS)) {
            navigation.getMenu().findItem(R.id.navigation_content).setIcon(ticketIcon);
        } else if (content.equals(EVENTS)) {
            navigation.getMenu().findItem(R.id.navigation_content).setIcon(eventIcon);
        }

        fm.beginTransaction().add(R.id.main_container, messagesFragment, "4").hide(messagesFragment).commit();
        fm.beginTransaction().add(R.id.main_container, eventsFragment, "3").hide(eventsFragment).commit();
        fm.beginTransaction().add(R.id.main_container, ticketsFragment, "2").hide(ticketsFragment).commit();
        fm.beginTransaction().add(R.id.main_container, homeFragment, "1").commit();

        Intent intent = new Intent(this, WebSocketListenerService.class);
        intent.putExtra("EXTRA_TOKEN", PreferenceUtils.getToken(this));
        startService(intent);
    }

    @Override
    public void addNotificationBadge(int number) {
        navigation.getOrCreateBadge(R.id.navigation_messages).setNumber(number);
    }

    @Override
    public void removeNotificationBadge() {
        navigation.removeBadge(R.id.navigation_messages);
    }

    @Override
    public void onItemClick(String item) {
        Toast.makeText(this, item, Toast.LENGTH_SHORT).show();
        if (item.equals("Билеты")) {
            changeContent(eventsFragment, ticketsFragment, TICKETS, ticketIcon);

        } else if (item.equals("События")) {
            changeContent(ticketsFragment, eventsFragment, EVENTS, eventIcon);
        }
    }

    private void changeContent(Fragment hideFragment, Fragment showFragment, String contentType, int icon) {
        fm.beginTransaction().hide(hideFragment).show(showFragment).commit();
        navigation.getMenu().findItem(R.id.navigation_content).setIcon(icon);
        content = contentType;
        PreferenceUtils.saveContentType(content, this);
    }
}
