package com.example.meetingapp.test_expandable.holders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetingapp.R;
import com.example.meetingapp.models.TestMegaCategory;
import com.google.android.material.chip.ChipGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class ProductViewHolder extends ChildViewHolder {

    private TextView chipGroup;


    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        chipGroup = itemView.findViewById(R.id.expand_button);
    }

    public void bind(TestMegaCategory testMegaCategory){
        chipGroup.setText(testMegaCategory.chipGroup);
    }
}
