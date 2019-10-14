package com.futurearts.hiltonnewproj.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.appcompat.app.AppCompatActivity;

import com.futurearts.hiltonnewproj.R;
import com.futurearts.hiltonnewproj.utils.ZoomFunctionality;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class PictureActivity extends AppCompatActivity {

    private ZoomFunctionality imageView;
    private static final String IMAGE_DIRECTORY = "/CustomImage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

//        Bundle b = getIntent().getExtras();
//        String uri = b.getString("uri");

        String uri = getIntent().getStringExtra(MediaStore.EXTRA_OUTPUT);


        imageView = findViewById(R.id.img);
        Bitmap bitmap =null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            InputStream imageStream = getApplicationContext().getContentResolver().openInputStream(Uri.fromFile(new File(uri)));
            bitmap = BitmapFactory.decodeStream(imageStream, null, options);

            imageStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


//        imageView.setImageBitmap(bitmap);

        ZoomFunctionality img = new ZoomFunctionality(this);
        img.setImageBitmap(bitmap);
        img.setMaxZoom(4f);
        setContentView(img);

    }


}