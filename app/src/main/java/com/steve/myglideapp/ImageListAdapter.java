package com.steve.myglideapp;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.IOException;

public class ImageListAdapter extends ArrayAdapter {
    private Context context;
    private LayoutInflater inflater;
    private String[] imageUrls;
    private Bitmap[] bitmaps;

    public ImageListAdapter(@NonNull Context context, String[] imageUrls, Bitmap[] thumbs) {
        super(context, R.layout.listview_item_image, imageUrls);

        this.context = context;
        this.imageUrls = imageUrls;
        this.bitmaps = thumbs;
        inflater = LayoutInflater.from(context);
    }

    int position = -1;
    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.listview_item_image, parent, false);
        }
        GlideApp.with(context)
                .load(imageUrls[position])
                .into((ImageView)convertView);
        return convertView;
    }

    private void showNoteInSystemTray(int index) {

        Intent target_intent = new Intent(context, ListviewActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, target_intent, 0);
        NotificationCompat.Builder noteBuilder = new NotificationCompat.Builder(context, "Alarm")
                .setContentTitle("Notificcation")
                .setContentText("From each image")
                .setSmallIcon(R.drawable.icon24v)
                .setLargeIcon(bitmaps[position])
                .setContentIntent(pendingIntent)
                .setAutoCancel(true); // click the note will cancel this notification.

        NotificationManagerCompat.from(context).notify(314, noteBuilder.build());
    }
}
