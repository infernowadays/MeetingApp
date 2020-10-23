package com.example.meetingapp.activities;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;
import butterknife.BindView;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.system.Os;
import android.view.View;
import android.widget.ImageView;

import com.example.meetingapp.R;
import com.example.meetingapp.fragments.profile_stepper.UserBasicInformationStepperFragmentFragment;
import com.steelkiwi.cropiwa.CropIwaView;
import com.steelkiwi.cropiwa.config.CropIwaSaveConfig;

import java.io.File;
import java.util.Objects;

public class CropImageActivity extends AppCompatActivity {

    CropIwaView cropView;
    ImageView crop_btn;
    int code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);

        cropView = (CropIwaView) findViewById(R.id.crop_view);
        crop_btn = (ImageView) findViewById(R.id.crop_btn);

        crop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Не уверен, что это удачный uri

                Uri resourceURI = Uri.fromFile(new File(getCacheDir()+"/cropped.png"));

                cropView.setImageUri(resourceURI);
                cropView.crop(new CropIwaSaveConfig.Builder(resourceURI)
                        .setCompressFormat(Bitmap.CompressFormat.PNG)
                        .setQuality(100)
                        .build());
                Intent cropped = new Intent();
                System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"+resourceURI.toString());
                cropped.putExtra("uri", resourceURI.toString());
                setResult(Activity.RESULT_OK, cropped);
                finish();
            }
        });

        Bundle bundle = getIntent().getExtras();

        assert bundle != null;
        code = bundle.getInt("request");

        if (code == UserBasicInformationStepperFragmentFragment.IMAGE_REQUEST){
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, code);
        } else if (code == UserBasicInformationStepperFragmentFragment.CAMERA_REQUEST){
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, code);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == code && resultCode == Activity.RESULT_OK && data != null){
            if (code == UserBasicInformationStepperFragmentFragment.IMAGE_REQUEST) {
                Uri imageUri = data.getData();
                cropView.setImage(BitmapFactory.decodeFile(getRealPathFromUri(imageUri)));
            } else if (code == UserBasicInformationStepperFragmentFragment.CAMERA_REQUEST){
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                cropView.setImage(photo);
            }
        } else {
            setResult(Activity.RESULT_CANCELED);
            finish();
        }
    }

    private String getRealPathFromUri(Uri contentUri) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, contentUri, filePathColumn, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = Objects.requireNonNull(cursor).getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }
}