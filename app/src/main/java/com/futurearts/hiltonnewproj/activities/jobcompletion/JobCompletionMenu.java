package com.futurearts.hiltonnewproj.activities.jobcompletion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.futurearts.hiltonnewproj.R;
import com.futurearts.hiltonnewproj.activities.BarcodeSearchActivity;
import com.futurearts.hiltonnewproj.utils.Constants;

public class JobCompletionMenu extends AppCompatActivity {

    LinearLayout layoutInsert, layoutSearch;
    ImageView btnBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_completion_menu);

        initViews();

        layoutInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(JobCompletionMenu.this, JobCompletionActivity.class);
                startActivity(i);

            }
        });


        layoutSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(JobCompletionMenu.this, BarcodeSearchActivity.class);
                i.putExtra(Constants.EXTRA_FROM_ACTIVITY,Constants.FROM_JOB_COMPLETION_MENU);
                startActivity(i);

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }

    private void initViews() {
        layoutInsert = findViewById(R.id.layPoTracking);
        layoutSearch = findViewById(R.id.laySearch1);
        btnBack=findViewById(R.id.btnBack);
    }
}
