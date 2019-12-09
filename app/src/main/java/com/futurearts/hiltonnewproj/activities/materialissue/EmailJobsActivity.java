package com.futurearts.hiltonnewproj.activities.materialissue;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.futurearts.hiltonnewproj.BuildConfig;
import com.futurearts.hiltonnewproj.R;
import com.futurearts.hiltonnewproj.adapters.CompletedAdapter;
import com.futurearts.hiltonnewproj.adapters.EmailJobsAdapter;
import com.futurearts.hiltonnewproj.modelclasses.MaterialIssueDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EmailJobsActivity extends AppCompatActivity {

    RadioGroup radioGroup;
    ImageView btnBack;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    EmailJobsAdapter factoryDataAdapter;
    Activity activity;
    DatabaseReference mDatabase;
    List<MaterialIssueDetails> materialIssueDetails;
    List<String> materialKeys;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factory_data);
        activity = this;
        mDatabase = FirebaseDatabase.getInstance().getReference().child(BuildConfig.BASE_TABLE).child(BuildConfig.EMAIL_JOBS_TABLE);
        initViews();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.one_set) {
                    /*Call DB function
                     * &
                     * Call recyclerViewDisp()*/
                    searchDbWithFacility("F1");


                } else if (checkedId == R.id.two_sets) {
                    /*Call DB function
                     * &
                     * Call recyclerViewDisp()*/
                    searchDbWithFacility("F2");


                } else if (checkedId == R.id.three_sets) {
                    /*Call DB function
                     * &
                     * Call recyclerViewDisp()*/
                    searchDbWithFacility("F3");


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

        materialIssueDetails = new ArrayList<>();
        materialKeys = new ArrayList<>();
        radioGroup = findViewById(R.id.radioGrpLoc);
        btnBack = findViewById(R.id.btnBack);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.mRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

    }

    private void recyclerViewDisp(List<MaterialIssueDetails> materialIssueDetails, List<String> materialKeys) {

        factoryDataAdapter = new EmailJobsAdapter(activity, materialIssueDetails);
        recyclerView.setAdapter(factoryDataAdapter);


    }

    public void searchDbWithFacility(String searchParam) {
        materialIssueDetails.clear();
        progressBar.setVisibility(View.VISIBLE);
        mDatabase.keepSynced(true);
        mDatabase.orderByChild("requiredLocation").equalTo(searchParam).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot post : dataSnapshot.getChildren()) {
                        MaterialIssueDetails productTable = post.getValue(MaterialIssueDetails.class);
                        materialIssueDetails.add(productTable);
                        materialKeys.add(post.getKey());
                    }
                    recyclerViewDisp(materialIssueDetails, materialKeys);
                } else {
                    recyclerViewDisp(materialIssueDetails, materialKeys);
                    Toast.makeText(EmailJobsActivity.this, "No data found.", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(EmailJobsActivity.this, "Database Error", Toast.LENGTH_SHORT).show();

            }
        });
    }






}
