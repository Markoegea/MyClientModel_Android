package com.kingmarco.myclientmodel.Threads;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.WorkerThread;

import java.io.InputStream;

/**Create a thread to bring from internet an image and se it to the imageView*/
public class ImageThreads extends Thread{
    String imageUrl;
    ImageView imageView;
    Activity activity;

    public ImageThreads(String imageUrl, ImageView imageView, Activity activity) {
        this.imageUrl = imageUrl;
        this.imageView = imageView;
        this.activity = activity;
    }

    @WorkerThread
    protected Bitmap doInBackground() {
        /**Bring the image from a URL and create a Bitmap with the it*/
        Bitmap imageBitmap = null;
        try {
            InputStream in = new java.net.URL(imageUrl).openStream();
            imageBitmap = BitmapFactory.decodeStream(in);
        } catch (Exception e){
            Log.e("Error", e.getMessage());
            e.printStackTrace();
            return null;
        }
        return imageBitmap;
    }

    @Override
    public void run() {
        /**Set the Bitmap in the Image view in the main Thread to avoid errors*/
        Bitmap image = doInBackground();
        if (image == null){
            return;
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageView.setImageBitmap(image);
            }
        });
    }
}
