package com.steve.myglideapp;

import android.content.Context;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import java.io.IOException;
// import java.lang.ref.WeakReference;

import static com.steve.myglideapp.ListviewActivity.thumbs;
import static com.steve.myglideapp.ListviewActivity.fdyImages;

public class getImageAsyncTask extends AsyncTask<Void, Integer, String>{
    Context parentCtx;
    // private WeakReference<ListviewActivity> refActivity;

    public getImageAsyncTask(ListviewActivity listActivity) {
      //  this.refActivity = new WeakReference<>(listActivity);
        parentCtx = listActivity;
    }

    @Override
    protected String doInBackground(Void... voids) {
        int len = fdyImages.length;
        try {
            for (int i = 0; i < len; i++ ) {
                thumbs[i] = Bitmap.createScaledBitmap(Picasso.with(parentCtx).load(fdyImages[i]).resize(200, 200).centerCrop().get(), 72, 72, false);
            }
        } catch (IOException e) {
            Toast.makeText(parentCtx, "Image loading failure.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return "Failure";
        }
        return "Success";
    }

    @Override
    protected void onPostExecute(String s) {
        Toast.makeText(parentCtx, "Notification images load completed.", Toast.LENGTH_SHORT).show();
        cancel(true);
        return;
    }
}
