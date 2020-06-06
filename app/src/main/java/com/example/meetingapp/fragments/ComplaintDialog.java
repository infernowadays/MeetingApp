package com.example.meetingapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.meetingapp.R;

import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class ComplaintDialog extends DialogFragment {

    private Callback callback;

    public static ComplaintDialog newInstance() {
        return new ComplaintDialog();
    }

    @OnClick(R.id.fullscreen_dialog_close)
    void closeDialog() {
        dismiss();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.EventsFilterDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_complaint_dialog, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
//        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow())
//                .getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    public interface Callback {
        void onActionClick();
    }
}