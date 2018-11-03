package com.steve.myglideapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    private static final int PARAM_READ_EXTERNAL_STORAGE = 222;
    boolean READ_EXTENAL=true;
    ImageView imgv;
    Button btnList;
    String InternetUrl="http://i.imgur.com/DvpvklR.png";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestStoragePermission();
        btnList = (Button)findViewById(R.id.btnList);
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ListviewActivity.class);
                startActivity(intent);
            }
        });
        imgv = (ImageView)findViewById(R.id.iv1);
        loadImageByInternetUrl();
        loadImageByResourceId();
        centCrop();
        fitCenter();

        Glide.with(this)
                .load("https://1.bp.blogspot.com/-9mkEkoaXFpc/VA80znRUWrI/AAAAAAAAYhU/uc3Vxv3GPnI/s1600/glide_logo.png")
                .into(imgv);

    }

    private void requestStoragePermission() {
        if(android.os.Build.VERSION.SDK_INT >=android.os.Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions, PARAM_READ_EXTERNAL_STORAGE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PARAM_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    READ_EXTENAL = true;
                } else {
                    READ_EXTENAL = false;
                    Toast.makeText(this, "Read external storage permission denided", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void loadImageByInternetUrl() {
        GlideApp
                .with(getBaseContext())
                .load(InternetUrl)
                .into(imgv);
    }

    private void loadImageByResourceId() {
        int rId = R.mipmap.ic_launcher;
        GlideApp.with(getBaseContext())
                .load(rId)
                .override(200, 50)
                .into((ImageView)findViewById(R.id.iv2));
    }
    private void centCrop() {
        String InternetUrl="http://i.imgur.com/DvpvklR.png";
        GlideApp
                .with(getBaseContext())
                .load(InternetUrl)
                .placeholder(R.drawable.loading)
                .centerCrop()
                .error(R.drawable.error)
                .into((ImageView)findViewById(R.id.iv3));
    }

    private void fitCenter() {
        String InternetUrl="http://i.imgur.com/DvpvklR.png";
        GlideApp
                .with(getBaseContext())
                .load(InternetUrl)
                .fitCenter()
                .into((ImageView)findViewById(R.id.iv4));
    }
}
