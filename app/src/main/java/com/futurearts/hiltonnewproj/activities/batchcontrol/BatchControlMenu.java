package com.futurearts.hiltonnewproj.activities.batchcontrol;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.futurearts.hiltonnewproj.R;

public class BatchControlMenu extends AppCompatActivity {

    LinearLayout layoutInsert,layoutSearch,layReportIssue;
    ImageView btnBack;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_control_menu);
        initViews();

        layoutInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(BatchControlMenu.this, BatchControlActivity.class);
                startActivity(i);

            }
        });


        layoutSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(BatchControlMenu.this, BatchControlSearchActivity.class);
                startActivity(i);
            }
        });

        layReportIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BatchControlMenu.this, ReportIssueActivity.class);
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
        layoutInsert = findViewById(R.id.layBatch);
        layoutSearch = findViewById(R.id.laySearch1);
        layReportIssue=findViewById(R.id.layReportIssue);
        btnBack=findViewById(R.id.btnBack);
    }
}
