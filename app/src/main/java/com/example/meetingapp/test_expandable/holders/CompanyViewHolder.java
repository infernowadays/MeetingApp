package com.example.meetingapp.test_expandable.holders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.meetingapp.MegaCat;
import com.example.meetingapp.R;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

public class CompanyViewHolder extends GroupViewHolder {

    private TextView name;


    public CompanyViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.expand_button);
    }

    public void bind(MegaCat megaCat) {
        name.setText(megaCat.getTitle());
    }
}
