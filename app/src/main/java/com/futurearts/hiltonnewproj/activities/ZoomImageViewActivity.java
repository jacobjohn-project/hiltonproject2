package com.futurearts.hiltonnewproj.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.futurearts.hiltonnewproj.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class ZoomImageViewActivity extends AppCompatActivity {

    PhotoView zoomImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_image_view);
        final String imageUrl=getIntent().getStringExtra("product_image");

        initView();
        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.img_placeholder)
                .networkPolicy(NetworkPolicy.OFFLINE)//user this for offline support
                .into(zoomImageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get()
                                .load(imageUrl)
                                .placeholder(R.drawable.img_placeholder)
                                .error(R.drawable.img_placeholder)//user this for offline support
                                .into(zoomImageView, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError(Exception e) {

                                    }


                                });
                    }

                });



    }

    public void initView(){
        zoomImageView=findViewById(R.id.zoomImageView);
    }
}
