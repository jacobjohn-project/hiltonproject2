package com.futurearts.hiltonnewproj.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;

import com.futurearts.hiltonnewproj.R;
import com.futurearts.hiltonnewproj.utils.SharedPref;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    LinearLayout laySearch, layPoTracking;
    SharedPref pref;
    ImageView btnMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();

        layPoTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, JobCompletionActivity.class);
                startActivity(i);
            }
        });

        laySearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MainActivity.this, MaterialIssueActivity.class);
                startActivity(i);
            }
        });

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showPopup(view);

            }
        });

    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.side_menu, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(this);
    }

    public void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(getString(R.string.log_out_confirm))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok_text), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        pref.resetSharedPref();
                        Intent intent = new Intent(MainActivity.this, SplashScreen.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton(getString(R.string.cancel_text), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    private void initViews() {

        layPoTracking = findViewById(R.id.layPoTracking);
        laySearch = findViewById(R.id.laySearch);
        btnMenu = findViewById(R.id.btnMenu);
        pref = new SharedPref(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*long lastUpdated = pref.getLastUpdatedTime();
        long currTime = System.currentTimeMillis();
        if (pref.getLastUpdatedTime() > 0) {
            if ((currTime - lastUpdated) / 1000 > Constants.LOG_OUT_TIME) {
                Intent i = new Intent(MainActivity.this, SplashScreen.class);
                startActivity(i);
                finish();
            }
        }*/
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.ic_menu_logout:
                showLogoutDialog();

                return true;

            default:
                return false;
        }
    }
}
