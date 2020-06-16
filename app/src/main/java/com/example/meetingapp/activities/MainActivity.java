package com.example.meetingapp.activities;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

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

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NotificationListener, BottomSheetFragment.ItemClickListener {

    private static Context context;
    private static Context applicationContext;
    final Fragment homeFragment = new HomeFragment();
    final Fragment eventsFragment = new EventsFragment();
    final Fragment ticketsFragment = new TicketsFragment();
    final Fragment messagesFragment = new MessagesFragment();
    final FragmentManager fm = getSupportFragmentManager();
    private final int EVENT_ICON = R.drawable.ic_events;
    private final int TICKET_ICON = R.drawable.ic_tickets;
    private final String EVENTS = "СОБЫТИЯ";
    private final String TICKETS = "БИЛЕТЫ";
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

    public static Context getMainContext() {
        return context;
    }

    public static Context getAppContext() {
        return applicationContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setLocale();

        context = this;
        applicationContext = getApplicationContext();

        navigation = findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        content = PreferenceUtils.getContentType(this);
        if (content.equals(EVENTS)) {
            navigation.getMenu().findItem(R.id.navigation_content).setIcon(EVENT_ICON);
            fm.beginTransaction().add(R.id.main_container, eventsFragment, "3").hide(eventsFragment).commit();
        } else if (content.equals(TICKETS)) {
            navigation.getMenu().findItem(R.id.navigation_content).setIcon(TICKET_ICON);
            fm.beginTransaction().add(R.id.main_container, ticketsFragment, "3").hide(ticketsFragment).commit();
        }


        fm.beginTransaction().add(R.id.main_container, messagesFragment, "2").hide(messagesFragment).commit();
        fm.beginTransaction().add(R.id.main_container, homeFragment, "1").commit();

        if(!isMyServiceRunning(WebSocketListenerService.class)){
            Intent intent = new Intent(this, WebSocketListenerService.class);
            intent.putExtra("EXTRA_TOKEN", PreferenceUtils.getToken(this));
            startService(intent);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (manager != null) {
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        }
        return false;
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
        if (item.equals(EVENTS)) {
            if (content.equals(TICKETS))
                changeContent(ticketsFragment, eventsFragment, EVENTS, EVENT_ICON);
        } else if (item.equals(TICKETS)) {
            if (content.equals(EVENTS))
                changeContent(eventsFragment, ticketsFragment, TICKETS, TICKET_ICON);
        }
    }

    private void changeContent(Fragment removeFragment, Fragment addFragment, String contentType, int icon) {
        fm.beginTransaction().remove(removeFragment).add(R.id.main_container, addFragment, "3").show(addFragment).commit();
        navigation.getMenu().findItem(R.id.navigation_content).setIcon(icon);
        active = addFragment;
        content = contentType;
        PreferenceUtils.saveContentType(content, this);
    }

    private void setLocale() {
        Configuration config = getApplicationContext().getResources().getConfiguration();
        Locale locale = new Locale("ru");
        Locale.setDefault(locale);
        config.locale = locale;
        getApplicationContext().getResources().updateConfiguration(config,
                getApplicationContext().getResources().getDisplayMetrics());
    }
}
