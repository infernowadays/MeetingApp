package com.example.meetingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetingapp.R;
import com.example.meetingapp.TransferCategories;
import com.example.meetingapp.activities.EditUserProfileCategoriesActivity;
import com.example.meetingapp.models.Category;
import com.example.meetingapp.models.MegaCategory;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryChipsAdapter extends RecyclerView.Adapter<CategoryChipsAdapter.ViewHolder> {
    private static final int UNSELECTED = -1;
    private int selectedItem = UNSELECTED;

    private RecyclerView recyclerView;

    private Context context;
    private List<MegaCategory> megaCategories;
    private TransferCategories transferCategories;

    private ArrayList<String> categories;

    public CategoryChipsAdapter(Context context, RecyclerView recyclerView, List<MegaCategory> megaCategories, TransferCategories transferCategories, ArrayList<String> categories) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.megaCategories = megaCategories;
        this.transferCategories = transferCategories;
        this.categories = categories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_category_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final MegaCategory megaCategory = megaCategories.get(position);

        holder.expandButton.setText(megaCategory.getName());

        for (Category category : megaCategory.getCategories()) {
            Chip chip = (Chip) LayoutInflater.from(context).inflate(R.layout.category_item, holder.chipGroup, false);
            chip.setText(category.getName());

            if (categories != null) {
                for (String categoryString : categories)
                    if (categoryString.contentEquals(chip.getText()))
                        chip.setChecked(true);
            }

            holder.chipGroup.addView(chip);

            chip.setOnCheckedChangeListener((compoundButton, checked) -> {
                if (checked) {
                    categories.add(String.valueOf(chip.getText()));
                    transferCategories.getResult(categories);

                    if (categories.size() == 5)
                        disableChips();

                } else {
                    categories.remove(String.valueOf(chip.getText()));
                    transferCategories.getResult(categories);

                    allowChips();
                }

                ((EditUserProfileCategoriesActivity) context).updateCounter(categories.size());
            });
        }

        holder.expandButton.setOnClickListener(v -> {
            ViewHolder currentHolder = (ViewHolder) recyclerView.findViewHolderForAdapterPosition(selectedItem);
            if (currentHolder != null) {
                currentHolder.expandButton.setSelected(false);
                currentHolder.expandableLayout.collapse();
            }

            int currentItemPosition = holder.getAdapterPosition();
            if (currentItemPosition == selectedItem) {
                selectedItem = UNSELECTED;
            } else {
                holder.expandButton.setSelected(true);
                holder.expandableLayout.expand();
                selectedItem = currentItemPosition;
            }

            if (categories.size() == 5)
                disableChips();
        });
    }

    @Override
    public int getItemCount() {
        return megaCategories.size();
    }

    private void disableChips() {
        ViewHolder currentHolder;
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            currentHolder = (ViewHolder) recyclerView.findViewHolderForAdapterPosition(i);

            if (currentHolder != null) {
                for (int j = 0; j < currentHolder.chipGroup.getChildCount(); j++) {
                    if (!((Chip) currentHolder.chipGroup.getChildAt(j)).isChecked()) {
                        ((Chip) currentHolder.chipGroup.getChildAt(j)).setCheckable(false);
                    }
                }
            }
        }
    }

    private void allowChips() {
        ViewHolder currentHolder;
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            currentHolder = (ViewHolder) recyclerView.findViewHolderForAdapterPosition(i);

            if (currentHolder != null) {
                for (int j = 0; j < currentHolder.chipGroup.getChildCount(); j++) {
                    ((Chip) currentHolder.chipGroup.getChildAt(j)).setCheckable(true);
                }
            }
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.expandable_layout)
        ExpandableLayout expandableLayout;

        @BindView(R.id.expand_button)
        TextView expandButton;

        @BindView(R.id.chip_group)
        ChipGroup chipGroup;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            expandableLayout.setInterpolator(new OvershootInterpolator());
        }
    }
}