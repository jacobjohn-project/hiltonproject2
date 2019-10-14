package com.futurearts.hiltonnewproj.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.futurearts.hiltonnewproj.BuildConfig;
import com.futurearts.hiltonnewproj.R;
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
    TextView txtPartName,txtRecDate,txtMovDate,txtLocFrom,txtLocTo,txtSignBy;
    ProgressBar progressBar,imgProgBar;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        mDatabase = FirebaseDatabase.getInstance().getReference().child(BuildConfig.BASE_TABLE).child(BuildConfig.PURCHASE_TABLE);

        initViews();
        String scanCode=getIntent().getStringExtra("product_code");
        if(scanCode!=null){
            checkDb(scanCode);
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
        imgProduct = findViewById(R.id.imgProduct);
        progressBar = findViewById(R.id.progressBar);
        scrollView=findViewById(R.id.scrollView);
        imgProgBar=findViewById(R.id.img_progBar);
        btnBack=findViewById(R.id.btnBack);
    }


    public void checkDb(String scanCode) {
        progressBar.setVisibility(View.VISIBLE);
        mDatabase.keepSynced(true);
        mDatabase.orderByChild("PO_Num").equalTo(scanCode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot post : dataSnapshot.getChildren()) {

                        imgProgBar.setVisibility(View.VISIBLE);
                        final ProductTable productTable =  post.getValue(ProductTable.class);

                        txtPartName.setText(productTable.PO_Part);
                        txtRecDate.setText(productTable.PO_Rec_date);
                        txtMovDate.setText(productTable.PO_Mv_date);
                        txtLocFrom.setText(productTable.PO_Loc_from);
                        txtLocTo.setText(productTable.PO_Loc_to);
                        txtSignBy.setText(productTable.PO_sign_by);

                        imgProduct.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(productTable.PO_image!=null){
                                    Intent i=new Intent(SearchResultActivity.this,ZoomImageViewActivity.class);
                                    i.putExtra("product_image",productTable.PO_image);
                                    startActivity(i);
                                }
                            }
                        });

                        Picasso.get()
                                .load(productTable.PO_image)
                                .placeholder(R.drawable.img_placeholder)
                                .networkPolicy(NetworkPolicy.OFFLINE)//user this for offline support
                                .into(imgProduct, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        Picasso.get()
                                                .load(productTable.PO_image)
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

                        /*Picasso.get().load(productTable.PO_image).placeholder(R.drawable.img_placeholder).into(imgProduct ,new com.squareup.picasso.Callback() {
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
                        });*/


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
}
