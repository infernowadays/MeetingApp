package com.example.meetingapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.meetingapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TicketsFragment extends ContentFragment {


    @Override
    public void openFilterDialog() {

    }

    @Override
    public void loadContent(List<String> categories) {

    }

    @Override
    public String getContentType() {
        return null;
    }
}
