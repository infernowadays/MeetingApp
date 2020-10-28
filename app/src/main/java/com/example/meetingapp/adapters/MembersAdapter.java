package com.example.meetingapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.meetingapp.R;
import com.example.meetingapp.activities.UserProfileActivity;
import com.example.meetingapp.interfaces.GetImageFromAsync;
import com.example.meetingapp.models.UserProfile;
import com.example.meetingapp.utils.images.DownloadImageTask;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.test.InstrumentationRegistry.getContext;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.ViewHolder> {
    private Context context;
    private List<UserProfile> members;

    public MembersAdapter(Context context, List<UserProfile> members) {
        this.members = members;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.member_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final UserProfile member = members.get(position);
        holder.textFirstName.setText(member.getFirstName() + " " + member.getLastName());

        if (member.getPhoto() != null) {
            holder.setImageProfile(member.getPhoto().getPhoto());
        } else {
            Glide.with(context).load(member.getPhoto()).into(holder.imageProfile);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UserProfileActivity.class);
                intent.putExtra("EXTRA_USER_PROFILE_ID", member.getId());
                getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    private Context getContext(){
        return context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements GetImageFromAsync {

        @BindView(R.id.text_first_name)
        TextView textFirstName;

        @BindView(R.id.image_profile)
        CircleImageView imageProfile;

        Bitmap bitmap;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setImageProfile(String photoUrl) {
            new DownloadImageTask(MembersAdapter.ViewHolder.this).execute(photoUrl);
        }

        @Override
        public void getResult(Bitmap bitmap) {
            if (bitmap != null) {
                imageProfile.setImageBitmap(bitmap);
                this.bitmap = bitmap;
            }
        }
    }
}
