package com.futurearts.hiltonnewproj.activities.batchcontrol;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.futurearts.hiltonnewproj.BuildConfig;
import com.futurearts.hiltonnewproj.R;
import com.futurearts.hiltonnewproj.activities.BarcodeSearchActivity;
import com.futurearts.hiltonnewproj.activities.ScannerActivity;
import com.futurearts.hiltonnewproj.activities.SearchResultActivity;
import com.futurearts.hiltonnewproj.modelclasses.BatchContraolDetails;
import com.futurearts.hiltonnewproj.modelclasses.JobCompletionDetails;
import com.futurearts.hiltonnewproj.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class BatchControlSearchActivity extends AppCompatActivity {

    ScrollView scrollView;
    ImageView imgProduct,btnBack;
    TextView txtPartNum,txtJobNum,txtBatchNum,txtQuantity,txtAddedBy,txtAddedDate,txtWorkCenter;
    EditText etSearch;
    ImageButton btnSearch,btnBarscan;
    ProgressBar progressBar,imgProgBar;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_control_search);

        mDatabase = FirebaseDatabase.getInstance().getReference().child(BuildConfig.BASE_TABLE).child(BuildConfig.BATCH_CONTROL_TABLE);


        initViews();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnBarscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(BatchControlSearchActivity.this, ScannerActivity.class);
                startActivityForResult(intent, 3);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String searchTxt = etSearch.getText().toString();

                if (!searchTxt.equals("")){

                    checkDb(searchTxt);

                }
            }
        });

    }

    private void initViews() {

        imgProduct = findViewById(R.id.imgProduct);
        btnBack = findViewById(R.id.btnBack);
        scrollView = findViewById(R.id.scrollView);
        etSearch=findViewById(R.id.etSearch);
        imgProgBar=findViewById(R.id.img_progBar);
        progressBar = findViewById(R.id.progressBar);
        btnSearch = findViewById(R.id.btnSearch);
        btnBarscan = findViewById(R.id.btnBarscan);

        txtPartNum = findViewById(R.id.txtPartNum);
        txtJobNum = findViewById(R.id.txtJobNum);
        txtBatchNum = findViewById(R.id.txtBatchNum);
        txtQuantity = findViewById(R.id.txtQuantity);
        txtAddedDate=findViewById(R.id.txtAddedDate);
        txtAddedBy=findViewById(R.id.txtAddedBy);
        txtWorkCenter=findViewById(R.id.txtWorkCenter);


    }

    public void checkDb(String scanCode) {


        progressBar.setVisibility(View.VISIBLE);
        mDatabase.keepSynced(true);
        mDatabase.orderByChild("job_number").equalTo(scanCode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                txtBatchNum.setText("");
                txtPartNum.setText("");
                txtJobNum.setText("");

                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot post : dataSnapshot.getChildren()) {
                        BatchContraolDetails productTable =  post.getValue(BatchContraolDetails.class);
                        imgProgBar.setVisibility(View.VISIBLE);

                        txtJobNum.setText(productTable.getJob_number());
                        txtPartNum.setText(productTable.getPart_number());
                        txtBatchNum.setText(productTable.getBatch_number());
                        txtQuantity.setText(productTable.getQuantity()+"");
                        txtAddedBy.setText(productTable.getAdded_by());
                        txtAddedDate.setText(productTable.getDate_time());
                        txtWorkCenter.setText(productTable.getWork_center());


                        Picasso.get().load(productTable.getImage_url()).placeholder(R.drawable.img_placeholder).into(imgProduct, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                //do smth when picture is loaded successfully
                                imgProgBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError(Exception ex) {
                                //do smth when there is picture loading error
                                imgProgBar.setVisibility(View.GONE);
                            }
                        });


                    }
                } else {
                    scrollView.setVisibility(View.GONE);
                    Toast.makeText(BatchControlSearchActivity.this, "No data found.", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
                scrollView.setVisibility(View.GONE);
                Toast.makeText(BatchControlSearchActivity.this, "Database Error", Toast.LENGTH_SHORT).show();

            }
        });

    }
}
