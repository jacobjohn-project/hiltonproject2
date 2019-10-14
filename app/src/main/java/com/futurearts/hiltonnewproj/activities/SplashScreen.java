package com.futurearts.hiltonnewproj.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.futurearts.hiltonnewproj.R;
import com.futurearts.hiltonnewproj.utils.Constants;
import com.futurearts.hiltonnewproj.utils.SharedPref;

public class SplashScreen extends AppCompatActivity {

    private static final int TIME = 1 * 1000;
    SharedPref pref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        pref=new SharedPref(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /*checkSharedPreference();*/
                openLoginorMain();
            }
        }, TIME);
    }

    public void checkSharedPreference(){
        if(pref.getLastUpdatedTime()==0) {
            openLoginorMain();
        }else{
            long lastUpdated=pref.getLastUpdatedTime();
            long currTime=System.currentTimeMillis();
            if(pref.getLastUpdatedTime()>0) {
                if ((currTime - lastUpdated) / 1000 > Constants.LOG_OUT_TIME) {
                    pref.resetSharedPref();
                    showLoggedOut();
                }else{
                    openLoginorMain();
                }
            }
        }
    }

    public void showLoggedOut(){
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
        builder.setMessage(getString(R.string.log_out_message))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok_text), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void openLoginorMain(){
        if (pref.getUserId().equals("")) {
            Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

}
