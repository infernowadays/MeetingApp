package com.example.meetingapp.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.meetingapp.R;
import com.example.meetingapp.api.RetrofitClient;
import com.example.meetingapp.models.Category;
import com.example.meetingapp.models.Ticket;
import com.example.meetingapp.utils.PreferenceUtils;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TicketActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.text)
    TextView toolbarTitle;
    @BindView(R.id.text_creator)
    TextView textCreator;
    @BindView(R.id.text_description)
    TextView textDescription;
    @BindView(R.id.text_address)
    TextView textAddress;
    @BindView(R.id.text_date)
    TextView textDate;
    @BindView(R.id.text_time)
    TextView textTime;
    @BindView(R.id.event_time)
    LinearLayout layoutEventTime;
    @BindView(R.id.chip_group)
    ChipGroup chipGroup;

    private Ticket ticket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);
        ButterKnife.bind(this);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbarTitle.setText("Велком");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.content_options_menu_for_creator, menu);
        return true;
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

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadTicket();
    }

    private void loadTicket() {
        String ticketId = getIntent().getStringExtra("EXTRA_TICKET_ID");

        Call<Ticket> call = RetrofitClient
                .getInstance(PreferenceUtils.getToken(this))
                .getApi()
                .getTicket(ticketId);

        call.enqueue(new Callback<Ticket>() {
            @Override
            public void onResponse(@NonNull Call<Ticket> call, @NonNull Response<Ticket> response) {
                ticket = response.body();

                if (ticket != null) {
                    chipGroup.removeAllViews();
                    for (Category category : ticket.getCategories()) {
                        Chip chip = (Chip) getLayoutInflater().inflate(R.layout.category_item, chipGroup, false);
                        chip.setText(category.getName());
                        chipGroup.addView(chip);
                    }
                    putTicket();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Ticket> call, @NonNull Throwable t) {

            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void putTicket() {
        textCreator.setText(ticket.getCreator().getFirstName() + " " +
                ticket.getCreator().getLastName());
        textDescription.setText(String.valueOf(ticket.getDescription()));
        textAddress.setText(String.valueOf(ticket.getAddress()));
        textDate.setText(ticket.getDate());
        textTime.setText(ticket.getTime());
    }
}