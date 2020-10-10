package com.example.meetingapp;

import android.os.Parcel;

import com.example.meetingapp.models.TestMegaCategory;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class MegaCat extends ExpandableGroup<TestMegaCategory> {
    public MegaCat(String title, List<TestMegaCategory> items) {
        super(title, items);
    }
}
