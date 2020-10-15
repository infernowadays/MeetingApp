package com.example.meetingapp.utils.images;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.example.meetingapp.interfaces.GetImageFromAsync;

import java.io.InputStream;
import java.util.Objects;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    private GetImageFromAsync bitmapListener;

    public DownloadImageTask(GetImageFromAsync bitmapListener) {
        this.bitmapListener = bitmapListener;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", Objects.requireNonNull(e.getMessage()));
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        this.bitmapListener.getResult(result);
    }
}