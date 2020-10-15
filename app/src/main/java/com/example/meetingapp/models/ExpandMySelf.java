package com.example.meetingapp.models;

import com.google.android.material.chip.ChipGroup;

public class ExpandMySelf {
    private String title;
    private boolean expanded;

    public ExpandMySelf(String title, boolean expanded) {
        this.title = title;

        this.expanded = expanded;
    }


    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
