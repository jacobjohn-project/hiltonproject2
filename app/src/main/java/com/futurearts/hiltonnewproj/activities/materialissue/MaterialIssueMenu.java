package com.futurearts.hiltonnewproj.activities.materialissue;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.futurearts.hiltonnewproj.R;
import com.futurearts.hiltonnewproj.activities.BarcodeSearchActivity;
import com.futurearts.hiltonnewproj.activities.PartNumberSearchActivity;
import com.futurearts.hiltonnewproj.utils.Constants;

public class MaterialIssueMenu extends AppCompatActivity {

    LinearLayout layoutInsert, layoutSearchByPartNumber,laySearchByJobNumber,layFactoryData,layCompleted,layEmailJobs;
    LinearLayout layTransferList;
    ImageView btnBack;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_issue_menu);
        initViews();

        layoutInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MaterialIssueMenu.this, MaterialIssueActivity.class);
                startActivity(i);

            }
        });


        layoutSearchByPartNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MaterialIssueMenu.this, PartNumberSearchActivity.class);
                startActivity(i);

            }
        });

        laySearchByJobNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MaterialIssueMenu.this, BarcodeSearchActivity.class);
                i.putExtra(Constants.EXTRA_FROM_ACTIVITY,Constants.FROM_MATERIAL_ISSUE_MENU);
                startActivity(i);

            }
        });

        layFactoryData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MaterialIssueMenu.this, FactoryDataActivity.class);
                startActivity(i);

            }
        });

        layCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MaterialIssueMenu.this, CompletedActivity.class);
                startActivity(i);
            }
        });

        layEmailJobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MaterialIssueMenu.this, EmailJobsActivity.class);
                startActivity(i);
            }
        });

        layTransferList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MaterialIssueMenu.this, TransferListActivity.class);
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
        layoutInsert = findViewById(R.id.laySearch);
        layoutSearchByPartNumber = findViewById(R.id.laySearch2);
        laySearchByJobNumber=findViewById(R.id.laySearch3);
        layFactoryData=findViewById(R.id.layFactory);
        layCompleted=findViewById(R.id.layCompleted);
        layEmailJobs=findViewById(R.id.layEmailJobs);
        layTransferList=findViewById(R.id.layTransferList);
        btnBack=findViewById(R.id.btnBack);
    }
}
