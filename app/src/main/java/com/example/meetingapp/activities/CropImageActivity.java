package com.example.meetingapp.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.loader.content.CursorLoader;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.example.meetingapp.R;
import com.example.meetingapp.fragments.profile_stepper.UserBasicInformationStepperFragmentFragment;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class CropImageActivity extends AppCompatActivity {

    private String currentPhotoPath;
    private int code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);


        Bundle bundle = getIntent().getExtras();

        assert bundle != null;
        code = bundle.getInt("request");

        if (code == UserBasicInformationStepperFragmentFragment.IMAGE_REQUEST){
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, code);
        } else if (code == UserBasicInformationStepperFragmentFragment.CAMERA_REQUEST){
            dispatchTakePictureIntent();
        }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri imageUri = null;
        if (requestCode == code && resultCode == Activity.RESULT_OK){
            if (code == UserBasicInformationStepperFragmentFragment.IMAGE_REQUEST) {
                imageUri = data.getData();
            } else if (code == UserBasicInformationStepperFragmentFragment.CAMERA_REQUEST){
                galleryAddPic();
                imageUri= Uri.fromFile(new File(currentPhotoPath));
                System.out.println("@@@@@@@@@@@@@@@@@@@@@@@"+imageUri);
            }
            Uri resultUri = null;
            try {
                resultUri = Uri.fromFile(createImageFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
            CropImage.activity(imageUri).setOutputUri(resultUri)
                    .setMultiTouchEnabled(true)
                    .setActivityTitle("WALK")
                    .setAllowRotation(false)
                    .setAllowFlipping(false)
                    .setMinCropResultSize(100, 100)
                    .setActivityMenuIconColor(R.color.colorPrimary)
                    .start(this);
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Intent cropData = new Intent();
                assert result != null;
                cropData.setData(result.getUri());
                Log.d("@@@@@@@@@@@@@@@@@@@@@@", "onActivityResult: CROPPED");
                setResult(Activity.RESULT_OK, cropData);
                finish();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Log.d("ERROR", "onActivityResult: Error");
                finish();
            }
        }else {
            setResult(Activity.RESULT_CANCELED);
            finish();
        }
    }



    private void dispatchTakePictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.meetingapp",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, code);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getFilesDir();
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
}