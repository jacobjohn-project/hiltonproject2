package com.futurearts.hiltonnewproj.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.futurearts.hiltonnewproj.BuildConfig;
import com.futurearts.hiltonnewproj.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BarcodeSearchActivity extends AppCompatActivity {

    ScrollView scrollView;
    ImageView imgProduct,btnBack;
    TextView txtPartName,txtRecDate,txtMovDate,txtLocFrom,txtLocTo,txtSignBy;
    EditText etSearch;
    ImageButton btnSearch,btnBarscan;
    ProgressBar progressBar,imgProgBar;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_barcode_search);

        mDatabase = FirebaseDatabase.getInstance().getReference().child(BuildConfig.BASE_TABLE).child(BuildConfig.JOB_COMPLETION_TABLE);

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
                Intent intent=new Intent(BarcodeSearchActivity.this,ScannerActivity.class);
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

        txtPartName = findViewById(R.id.txtPartName);
        txtRecDate = findViewById(R.id.txtRecDate);
        txtMovDate = findViewById(R.id.txtMovDate);
        txtLocFrom = findViewById(R.id.txtLocFrom);
        txtLocTo = findViewById(R.id.txtLocTo);
        txtSignBy = findViewById(R.id.txtSignBy);
        btnSearch = findViewById(R.id.btnSearch);
        btnBarscan = findViewById(R.id.btnBarscan);
        imgProduct = findViewById(R.id.imgProduct);
        progressBar = findViewById(R.id.progressBar);
        btnBack=findViewById(R.id.btnBack);
        etSearch=findViewById(R.id.etSearch);
        scrollView=findViewById(R.id.scrollView);
        imgProgBar=findViewById(R.id.img_progBar);
    }


    public void checkDb(String scanCode) {
        Intent i=new Intent(BarcodeSearchActivity.this,SearchResultActivity.class);
        i.putExtra("job_Num",scanCode);
        startActivity(i);

        /*progressBar.setVisibility(View.VISIBLE);
        mDatabase.keepSynced(true);
        mDatabase.orderByChild("PO_Num").equalTo(scanCode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot post : dataSnapshot.getChildren()) {

                        imgProgBar.setVisibility(View.VISIBLE);
                        ProductTable productTable =  post.getValue(ProductTable.class);

                        txtPartName.setText(productTable.PO_Part);
                        txtRecDate.setText(productTable.PO_Rec_date);
                        txtMovDate.setText(productTable.PO_Mv_date);
                        txtLocFrom.setText(productTable.PO_Loc_from);
                        txtLocTo.setText(productTable.PO_Loc_to);
                        txtSignBy.setText(productTable.PO_sign_by);


                        Picasso.get().load(productTable.PO_image).placeholder(R.drawable.img_placeholder).into(imgProduct ,new com.squareup.picasso.Callback() {
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
                    Toast.makeText(BarcodeSearchActivity.this, "No data found.", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
                scrollView.setVisibility(View.GONE);
                Toast.makeText(BarcodeSearchActivity.this, "Database Error", Toast.LENGTH_SHORT).show();

            }
        });*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 3) {


                String message = data.getStringExtra("MESSAGE");
                etSearch.setText(message);

                checkDb(message);


            }
        }

    }
}
