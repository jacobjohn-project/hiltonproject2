package com.futurearts.hiltonnewproj.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.futurearts.hiltonnewproj.BuildConfig;
import com.futurearts.hiltonnewproj.R;
import com.futurearts.hiltonnewproj.modelclasses.MaterialIssueDetails;
import com.futurearts.hiltonnewproj.modelclasses.ProductTable;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class SearchResultActivity extends AppCompatActivity {

    ScrollView scrollView;
    ImageView imgProduct,btnBack;
    TextView txtJoborPartNumber,txtQtyShortage,txtReqLocation,txtIsUrgent,txtSavedDate,txtSignBy;
    TextView tvJobOrPart;
    ProgressBar progressBar,imgProgBar;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        mDatabase = FirebaseDatabase.getInstance().getReference().child(BuildConfig.BASE_TABLE).child(BuildConfig.MATERIAL_ISSUE_TABLE);

        initViews();
        String jobNumber=getIntent().getStringExtra("job_Num");
        if(jobNumber!=null){
            checkDbUsingJobNumber(jobNumber);
        }
        String partNumber=getIntent().getStringExtra("part_Num");
        if(partNumber!=null){
            checkDbUsingPartNumber(partNumber);
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }

    private void initViews() {

        txtJoborPartNumber = findViewById(R.id.txtJoborPartNumber);
        txtQtyShortage = findViewById(R.id.txtQtyShortage);
        txtReqLocation = findViewById(R.id.txtReqLocation);
        txtIsUrgent = findViewById(R.id.txtIsUrgent);
        txtSavedDate = findViewById(R.id.txtSavedDate);
        txtSignBy = findViewById(R.id.txtSignBy);
        imgProduct = findViewById(R.id.imgProduct);
        progressBar = findViewById(R.id.progressBar);
        scrollView=findViewById(R.id.scrollView);
        imgProgBar=findViewById(R.id.img_progBar);
        btnBack=findViewById(R.id.btnBack);
        tvJobOrPart=findViewById(R.id.tvJobOrPart);
    }


    public void checkDbUsingJobNumber(String scanCode) {
        progressBar.setVisibility(View.VISIBLE);
        mDatabase.keepSynced(true);
        mDatabase.orderByChild("job_Num").equalTo(scanCode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot post : dataSnapshot.getChildren()) {

                        imgProgBar.setVisibility(View.VISIBLE);
                        final MaterialIssueDetails materialIssueDetails =  post.getValue(MaterialIssueDetails.class);
                        populateViews(materialIssueDetails,true);


                    }
                } else {
                    scrollView.setVisibility(View.GONE);
                    Toast.makeText(SearchResultActivity.this, "No data found.", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
                scrollView.setVisibility(View.GONE);
                Toast.makeText(SearchResultActivity.this, "Database Error", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void checkDbUsingPartNumber(String scanCode) {
        progressBar.setVisibility(View.VISIBLE);
        mDatabase.keepSynced(true);
        mDatabase.orderByChild("part_Num").equalTo(scanCode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot post : dataSnapshot.getChildren()) {

                        imgProgBar.setVisibility(View.VISIBLE);
                        final MaterialIssueDetails materialIssueDetails =  post.getValue(MaterialIssueDetails.class);

                        populateViews(materialIssueDetails,false);


                    }
                } else {
                    scrollView.setVisibility(View.GONE);
                    Toast.makeText(SearchResultActivity.this, "No data found.", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
                scrollView.setVisibility(View.GONE);
                Toast.makeText(SearchResultActivity.this, "Database Error", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void populateViews(final MaterialIssueDetails materialIssueDetails,boolean isJob){
        if(isJob){
            tvJobOrPart.setText("Job Number");
            txtJoborPartNumber.setText(materialIssueDetails.getJob_Num());
        }else{
            tvJobOrPart.setText("Part Number");
            txtJoborPartNumber.setText(materialIssueDetails.getPart_Num());
        }

        txtQtyShortage.setText(materialIssueDetails.getQty_shortage()+"");
        txtReqLocation.setText(materialIssueDetails.getRequiredLocation());
        if(materialIssueDetails.isUrgent()){
            txtIsUrgent.setText("YES");
        }else{
            txtIsUrgent.setText("NO");
        }
        txtSavedDate.setText(materialIssueDetails.getSaved_date());
        txtSignBy.setText(materialIssueDetails.getWho());
        /*txtRecDate.setText(materialIssueDetails.PO_Rec_date);
        txtMovDate.setText(materialIssueDetails.PO_Mv_date);
        txtLocFrom.setText(materialIssueDetails.PO_Loc_from);
        txtLocTo.setText(materialIssueDetails.PO_Loc_to);
        txtSignBy.setText(materialIssueDetails.PO_sign_by);*/

        imgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(materialIssueDetails.materialJobImage!=null){
                    Intent i=new Intent(SearchResultActivity.this,ZoomImageViewActivity.class);
                    i.putExtra("product_image",materialIssueDetails.materialJobImage);
                    startActivity(i);
                }
            }
        });


        Picasso.get()
                .load(materialIssueDetails.getMaterialJobImage())
                .placeholder(R.drawable.img_placeholder)
                .networkPolicy(NetworkPolicy.OFFLINE)//user this for offline support
                .into(imgProduct, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get()
                                .load(materialIssueDetails.materialJobImage)
                                .placeholder(R.drawable.img_placeholder)
                                .error(R.drawable.img_placeholder)//user this for offline support
                                .into(imgProduct, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError(Exception e) {

                                    }


                                });
                    }

                });
    }

}
