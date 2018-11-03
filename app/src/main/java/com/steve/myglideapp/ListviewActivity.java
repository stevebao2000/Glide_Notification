package com.steve.myglideapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.Request;
import okhttp3.Call;
import okhttp3.Callback;
public class ListviewActivity extends AppCompatActivity {

    public static String[] fdyImages = {
            "http://i.imgur.com/rT5vXE1.jpg",
            "http://i.imgur.com/aIy5R2k.jpg",
            "http://i.imgur.com/MoJs9pT.jpg",
            "http://i.imgur.com/rLR2cyc.jpg",
            "http://i.imgur.com/SEPdUIx.jpg",
            "http://i.imgur.com/fUX7EIB.jpg",
            "http://i.imgur.com/syELajx.jpg",
            "http://i.imgur.com/Z3QjilA.jpg"
    };

    public static Bitmap[] thumbs = new Bitmap[fdyImages.length];
    getImageAsyncTask asyncTask;
    boolean usePicasso = false;
    OkHttpClient okHttpClient = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);

        final ListView listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(
                new ImageListAdapter(ListviewActivity.this, fdyImages, thumbs)
        );

        // Set an item click listener for ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                showNotificationInSystemTray(position);
            }
        });

        // Glide v4 changed its interface. It is hard to get the bitmap from glide now.
        // We use Picasso to get the thumb nails bitmap for the notifications for each image.
        if (usePicasso) {
            asyncTask = new getImageAsyncTask(this);
            asyncTask.execute();
        } else {
            // We can also use okHttp3 to download the images. We send all okhttp3 requests together.
            for (int i = 0; i < fdyImages.length; i++)
                getImageByOkHttpClient(fdyImages[i], i);
        }
    }

    private void getImageByOkHttpClient(String url, int index) {
        Log.d("okhttp3: ", "-------------> image " + index + " loading...");
        final int idx = index;
        final Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //Handle the error
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    final Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());

                    // Remember to set the bitmap in the main thread.
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            // Crop the image to be square of short side.
                            int w = bitmap.getWidth();
                            int h = bitmap.getHeight();
                            int width, height, cx, cy;
                            if ( h < w ) {
                                width = h;
                                height = h;
                                cy = 0;
                                cx = (int)((w - h)/2);
                            } else {
                                width = w;
                                height = w;
                                cx = 0;
                                cy = (int)((h-w)/2);
                            }
                            thumbs[idx]=Bitmap.createScaledBitmap(Bitmap.createBitmap(bitmap, cx, cy, width, height), 72, 72, false);
                            Log.d("okhttp3: ", "==========>> image " + idx + " loaded.");
                        }
                    });
                }else {
                    //Handle the error
                }
            }
        });
    }
    private void showNotificationInSystemTray(int idx) {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default",
                    "com.steve.myglideapp",
                    NotificationManager.IMPORTANCE_DEFAULT);

            channel.setDescription("Notification Demo");
            mNotificationManager.createNotificationChannel(channel);
        }
        // The above part is just for Android SDK 26+

        Intent target_intent = new Intent(getApplicationContext(), ListviewActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, target_intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri sound_uri = Uri.parse("android.resource://com.steve.myglideapp/" + R.raw.vanish);
        NotificationCompat.Builder noteBuilder = new NotificationCompat.Builder(getBaseContext(), "default")
                .setContentTitle("Notification Image")
                .setContentText("Based on the image you clicked.")
                .setLargeIcon(thumbs[idx])
                .setSmallIcon(R.drawable.icon24v)
                // .setSound(sound_uri)
                .setVibrate(new long[] { 1000, 1000, 1000})
                .setLights(Color.RED, 3000, 3000) // only work on some phone
                 .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                //   .setColor(getResources().getColor(R.color.blue3))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true); // click the note will cancel this notification.

        noteBuilder.setContentIntent(pendingIntent);
        mNotificationManager.notify(idx, noteBuilder.build());
        //NotificationManagerCompat.from(getApplicationContext()).notify(6225415, noteBuilder.build());
    }
}