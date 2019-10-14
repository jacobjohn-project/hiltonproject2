package com.futurearts.hiltonnewproj.activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.futurearts.hiltonnewproj.R;
import com.google.firebase.database.DatabaseReference;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScannerActivity extends AppCompatActivity {

    Button BtnTryAgain;
    String CODE = "", ACTION = "", BALANCE = "0";
    ProgressBar progressBar;
    Activity activity;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent intent = getIntent();
        ACTION = intent.getStringExtra("ACTION");
        activity = this;
//        new IntentIntegrator(activity).initiateScan();

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("Scan a Code");
        integrator.setCaptureActivity(PortraitCaptureActivity.class);
        integrator.setCameraId(0); // Use a specific camera of the device
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();


        setContentView(R.layout.activity_scanner);

        BtnTryAgain = (Button) findViewById(R.id.BtnTryAgain);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        BtnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IntentIntegrator(activity).initiateScan();
            }
        });

        //checkDb("1");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                progressBar.setVisibility(View.GONE);
                BtnTryAgain.setVisibility(View.VISIBLE);
                Log.d("QRCodeScanner", "Cancelled scan");
                Intent intent = new Intent();
                intent.putExtra("MESSAGE", "");
                setResult(0, intent);
                finish();
                //Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                progressBar.setVisibility(View.VISIBLE);
                BtnTryAgain.setVisibility(View.GONE);
                Log.d("QRCodeScanner", "Scanned");
                //Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                System.out.println("CHECK--> : " + result.getContents());
                CODE = result.getContents();

                Intent intent = new Intent();
                intent.putExtra("MESSAGE", CODE);
                setResult(-1, intent);
                finish();
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);

        }
    }




}
