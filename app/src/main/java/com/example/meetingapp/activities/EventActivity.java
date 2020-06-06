package com.example.meetingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.meetingapp.R;
import com.example.meetingapp.fragments.EventChatFragment;
import com.example.meetingapp.fragments.EventInfoFragment;
import com.example.meetingapp.models.Event;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventActivity extends AppCompatActivity {

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        ButterKnife.bind(this);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragment(new EventInfoFragment(), "Информация");
        viewPagerAdapter.addFragment(new EventChatFragment(), "Чат");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        Intent intent = getIntent();
        if (intent.hasExtra("EXTRA_ACTIVE_TAB")) {
            selectActiveTab(intent.getIntExtra("EXTRA_ACTIVE_TAB", 1));
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        TextView textView = findViewById(R.id.text);
        textView.setText("Велком");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.content_options_menu_for_creator, menu);
        return true;
    }

    private void selectActiveTab(int index) {
        Objects.requireNonNull(tabLayout.getTabAt(index)).select();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_stop:
                // do your code
                return true;
            case R.id.menu_cancel:
                // do your code
                return true;
            case R.id.action_edit:
                openEventEditor();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openEventEditor() {
        Intent intent = new Intent(EventActivity.this, CreateEventActivity.class);
        intent.putExtra("action", "edit");

        intent.putExtra("EXTRA_EVENT", EventInfoFragment.getEvent());

        EventActivity.this.startActivity(intent);
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
}
