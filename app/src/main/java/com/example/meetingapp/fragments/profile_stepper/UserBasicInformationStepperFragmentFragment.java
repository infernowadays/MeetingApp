package com.example.meetingapp.fragments.profile_stepper;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.loader.content.CursorLoader;

import com.example.meetingapp.DownloadImageTask;
import com.example.meetingapp.GetImageFromAsync;
import com.example.meetingapp.IUserProfileManager;
import com.example.meetingapp.R;
import com.example.meetingapp.api.RetrofitClient;
import com.example.meetingapp.models.ProfilePhoto;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class UserBasicInformationStepperFragmentFragment extends Fragment implements BlockingStep,
        DatePickerDialog.OnDateSetListener, GetImageFromAsync {

    private static final int IMAGE_REQUEST = 100;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    @BindView(R.id.text_birth_date)
    MaterialEditText textBirthDate;
    @BindView(R.id.image_profile)
    CircleImageView imageProfile;
    private IUserProfileManager iUserProfileManager;
    private FragmentActivity mContext;
    private String imageUrl;
    private Bitmap bitmap;
    private String pattern = "yyyy-MM-dd";
    private Date date;

    public static UserBasicInformationStepperFragmentFragment newInstance() {
        return new UserBasicInformationStepperFragmentFragment();
    }

    private static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_info_stepper_fragment, container, false);
        ButterKnife.bind(this, view);

        date = new Date();

        return view;
    }

    @OnClick(R.id.text_birth_date)
    void setTextDate() {
        showDatePickerDialog();
    }

    @OnClick(R.id.image_profile)
    void openImage() {
        verifyStoragePermissions(requireActivity());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, IMAGE_REQUEST);
        }
    }

    private String getRealPathFromUri(Uri contentUri) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(requireActivity(), contentUri, filePathColumn, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = Objects.requireNonNull(cursor).getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            upload(imageUri);
        }
    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        String date = Objects.requireNonNull(textBirthDate.getText()).toString();
        iUserProfileManager.saveBirthDate(date);

        callback.goToNextStep();
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    private void showDatePickerDialog() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireActivity(),
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
        datePickerDialog.show();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = (FragmentActivity) context;

        if (context instanceof IUserProfileManager) {
            iUserProfileManager = (IUserProfileManager) context;
        } else {
            throw new IllegalStateException("Activity must implement IUserProfileManager interface!");
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        String strMonth = String.valueOf(month + 1);
        if (month < 10) {
            strMonth = "0" + (month + 1);
        }

        String strDay = String.valueOf(day);
        if (day < 10) {
            strDay = "0" + day;
        }

        String dateString = year + "-" + strMonth + "-" + strDay;
        textBirthDate.setText(dateString);
        setDateFromString(dateString);
    }

    private void setDateFromString(String dateString) {
        DateFormat format = new SimpleDateFormat(pattern, Locale.ENGLISH);
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void upload(Uri imageUri) {
        String fullPath = getRealPathFromUri(imageUri);
        File file = new File(fullPath);

        RequestBody requestFile = RequestBody.create(file, MediaType.parse(
                Objects.requireNonNull(requireActivity().getContentResolver().getType(imageUri))));

        Map<String, RequestBody> map = new HashMap<>();
        map.put("photo\"; filename=\"" + fullPath + "\"", requestFile);


        RetrofitClient.needsHeader(true);
        RetrofitClient.setToken("333afa3e66653dfd524c13fa746550fbe8e67ba2");
        Call<ProfilePhoto> call = RetrofitClient
                .getInstance("333afa3e66653dfd524c13fa746550fbe8e67ba2")
                .getApi()
                .uploadFile("145", map);

        call.enqueue(new Callback<ProfilePhoto>() {
            @Override
            public void onResponse(@NonNull Call<ProfilePhoto> call, @NonNull Response<ProfilePhoto> response) {
                ProfilePhoto profilePhoto = response.body();
                if (profilePhoto != null) {
                    imageUrl = profilePhoto.getPhoto();
                    new DownloadImageTask(imageProfile, UserBasicInformationStepperFragmentFragment.this).execute(imageUrl);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProfilePhoto> call, @NonNull Throwable t) {
                Log.d("error", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (bitmap != null)
            imageProfile.setImageBitmap(bitmap);
    }

    @Override
    public void getResult(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
