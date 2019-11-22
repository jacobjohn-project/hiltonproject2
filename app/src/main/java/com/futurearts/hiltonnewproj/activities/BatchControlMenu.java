package com.futurearts.hiltonnewproj.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.futurearts.hiltonnewproj.R;

public class BatchControlMenu extends AppCompatActivity {

    LinearLayout layoutInsert,layoutSearch;
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
        btnBack=findViewById(R.id.btnBack);
    }
}
