package com.futurearts.hiltonnewproj.activities.materialissue;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.futurearts.hiltonnewproj.interfaces.CompletedListener;
import com.futurearts.hiltonnewproj.adapters.FactoryDataAdapter;
import com.futurearts.hiltonnewproj.modelclasses.MaterialIssueDetails;
import com.futurearts.hiltonnewproj.utils.SharedPref;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FactoryDataActivity extends AppCompatActivity implements CompletedListener {

    RadioGroup radioGroup;
    ImageView btnBack;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    FactoryDataAdapter factoryDataAdapter;
    Activity activity;
    DatabaseReference mDatabase;
    DatabaseReference mDatabaseCompleted;
    List<MaterialIssueDetails> materialIssueDetails;
    List<String> materialKeys;

    SharedPref pref;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factory_data);
        activity =this;
        mDatabase = FirebaseDatabase.getInstance().getReference().child(BuildConfig.BASE_TABLE).child(BuildConfig.MATERIAL_ISSUE_TABLE);
        mDatabaseCompleted = FirebaseDatabase.getInstance().getReference().child(BuildConfig.BASE_TABLE).child(BuildConfig.COMPLETED_TABLE);
        initViews();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(checkedId == R.id.one_set){
                    /*Call DB function
                    * &
                    * Call recyclerViewDisp()*/
                    searchDbWithFacility("F1");



                }else if(checkedId == R.id.two_sets){
                    /*Call DB function
                     * &
                     * Call recyclerViewDisp()*/
                    searchDbWithFacility("F2");


                } else if(checkedId == R.id.three_sets){
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

        pref = new SharedPref(this);
        materialIssueDetails=new ArrayList<>();
        materialKeys=new ArrayList<>();
        radioGroup = findViewById(R.id.radioGrpLoc);
        btnBack = findViewById(R.id.btnBack);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.mRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

    }

    private void recyclerViewDisp(List<MaterialIssueDetails> materialIssueDetails,List<String> materialKeys){

        factoryDataAdapter = new FactoryDataAdapter(activity,materialIssueDetails,materialKeys,this);
        recyclerView.setAdapter(factoryDataAdapter);


    }

    public void searchDbWithFacility(String searchParam){
        materialIssueDetails.clear();
        progressBar.setVisibility(View.VISIBLE);
        mDatabase.keepSynced(true);
        mDatabase.orderByChild("requiredLocation").equalTo(searchParam).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot post : dataSnapshot.getChildren()) {
                        MaterialIssueDetails productTable =  post.getValue(MaterialIssueDetails.class);
                        materialIssueDetails.add(productTable);
                        materialKeys.add(post.getKey());
                    }
                    recyclerViewDisp(materialIssueDetails,materialKeys);
                } else {
                    recyclerViewDisp(materialIssueDetails,materialKeys);
                    Toast.makeText(FactoryDataActivity.this, "No data found.", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(FactoryDataActivity.this, "Database Error", Toast.LENGTH_SHORT).show();

            }
        });
    }



    @Override
    public void onCompleted(int position, MaterialIssueDetails materialIssue, String key) {
        showConfirmDialog(this,position,materialIssue,key);
    }


    public void showConfirmDialog(final FactoryDataActivity factoryDataActivity, final int position, final MaterialIssueDetails materialIssue, final String key) {
        AlertDialog.Builder builder = new AlertDialog.Builder(FactoryDataActivity.this);
        builder.setMessage(getString(R.string.completed_confirm))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok_text), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        materialIssue.setDoneBy(pref.getUserName());
                        setCompleted(position,materialIssue,key);



                    }
                })
                .setNegativeButton(getString(R.string.cancel_text), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void setCompleted(int position,MaterialIssueDetails materialIssue, String key){

        mDatabaseCompleted.push().setValue(materialIssue);
        mDatabase.child(key).setValue(null);
        materialIssueDetails.remove(position);
        materialKeys.remove(position);
        factoryDataAdapter.notifyItemRemoved(position);
        factoryDataAdapter.notifyItemRangeChanged(position,materialIssueDetails.size());


    }


}
