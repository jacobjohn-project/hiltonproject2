package com.futurearts.hiltonnewproj.activities.materialissue;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.futurearts.hiltonnewproj.R;
import com.futurearts.hiltonnewproj.modelclasses.MaterialIssueDetails;

import java.util.List;

public class FactoryDataActivity extends AppCompatActivity {

    RadioGroup radioGroup;
    ImageView btnBack;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    FactoryDataAdapter factoryDataAdapter;
    Activity activity;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factory_data);
        activity =this;
        initViews();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(checkedId == R.id.one_set){
                    /*Call DB function
                    * &
                    * Call recyclerViewDisp()*/



                }else if(checkedId == R.id.two_sets){
                    /*Call DB function
                     * &
                     * Call recyclerViewDisp()*/


                } else if(checkedId == R.id.three_sets){
                    /*Call DB function
                     * &
                     * Call recyclerViewDisp()*/


                }

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

        radioGroup = findViewById(R.id.radioGrpLoc);
        btnBack = findViewById(R.id.btnBack);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.mRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

    }

    private void recyclerViewDisp(List<MaterialIssueDetails> materialIssueDetails){

        factoryDataAdapter = new FactoryDataAdapter(activity,materialIssueDetails);
        recyclerView.setAdapter(factoryDataAdapter);


    }


}
