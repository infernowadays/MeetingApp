package com.example.meetingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meetingapp.R;
import com.example.meetingapp.api.RetrofitClient;
import com.example.meetingapp.models.Complaint;
import com.example.meetingapp.utils.PreferenceUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComplaintsReasonsAdapter extends RecyclerView.Adapter<ComplaintsReasonsAdapter.ViewHolder> {

    private List<String> complaintsReasons;
    private Context context;
    private int contentId;
    private String contentType;
    private DialogFragment dialogFragment;

    public ComplaintsReasonsAdapter(Context context, List<String> complaintsReasons, int contentId, String contentType, DialogFragment dialogFragment) {
        this.complaintsReasons = complaintsReasons;
        this.context = context;
        this.contentId = contentId;
        this.contentType = contentType;
        this.dialogFragment = dialogFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.complaints_reasons_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String complaintReason = complaintsReasons.get(position);

        holder.text.setText(String.valueOf(complaintReason));

        holder.itemView.setOnClickListener(v -> {
            sendComplaint(complaintReason);
        });
    }

    private void sendComplaint(String message) {
        Call<Complaint> call = RetrofitClient
                .getInstance(PreferenceUtils.getToken(getContext()))
                .getApi()
                .sendComplaint(new Complaint(message, contentId, contentType));

        call.enqueue(new Callback<Complaint>() {
            @Override
            public void onResponse(@NonNull Call<Complaint> call, @NonNull Response<Complaint> response) {
                Toast.makeText(getContext(), "Жалобы была успешно отправлена!", Toast.LENGTH_SHORT).show();
                dialogFragment.dismiss();

            }

            @Override
            public void onFailure(@NonNull Call<Complaint> call, @NonNull Throwable t) {
                int a = 5;
            }
        });
    }

    private Context getContext() {
        return context;
    }

    @Override
    public int getItemCount() {
        return complaintsReasons.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text)
        TextView text;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
