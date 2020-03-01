package com.futurearts.hiltonnewproj.activities.materialissue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.futurearts.hiltonnewproj.BuildConfig;
import com.futurearts.hiltonnewproj.R;
import com.futurearts.hiltonnewproj.adapters.FactoryDataAdapter;
import com.futurearts.hiltonnewproj.adapters.TransferListAdapter;
import com.futurearts.hiltonnewproj.interfaces.CompletedListener;
import com.futurearts.hiltonnewproj.modelclasses.MaterialIssueDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TransferListActivity extends AppCompatActivity implements CompletedListener {

    TextView tvNoItems;
    RecyclerView recyclerTransferList;
    ProgressBar progressBar;
    List<MaterialIssueDetails> materialIssueDetails;
    List<String> materialKeys;
    TransferListAdapter transferListAdapter;
    ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_list);
        initViews();
    }

    public void initViews(){
        tvNoItems=findViewById(R.id.tvNoItems);
        recyclerTransferList=findViewById(R.id.recyclerTransferList);
        recyclerTransferList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerTransferList.setItemAnimator(new DefaultItemAnimator());
        recyclerTransferList.setNestedScrollingEnabled(false);
        progressBar=findViewById(R.id.progressBar);
        materialIssueDetails=new ArrayList<>();
        materialKeys=new ArrayList<>();
        btnBack=findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        fetchTransferList();
    }

    public void fetchTransferList(){
        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference().child(BuildConfig.BASE_TABLE).child(BuildConfig.MATERIAL_ISSUE_TABLE);
        mDatabase.keepSynced(true);
        mDatabase.orderByChild("whereTo").equalTo("").addListenerForSingleValueEvent(new ValueEventListener() {
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
                    Toast.makeText(TransferListActivity.this, "No data found.", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void recyclerViewDisp(List<MaterialIssueDetails> materialIssueDetails,List<String> materialKeys){
        transferListAdapter = new TransferListAdapter(this,materialIssueDetails,materialKeys,this);
        recyclerTransferList.setAdapter(transferListAdapter);
    }

    @Override
    public void onCompleted(int position, MaterialIssueDetails materialIssue, String key) {
        DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference().child(BuildConfig.BASE_TABLE).child(BuildConfig.MATERIAL_ISSUE_TABLE);
        mDatabase.child(key).setValue(materialIssue);
        materialIssueDetails.remove(position);
        materialKeys.remove(position);

        if(materialIssueDetails.size()==0){
            tvNoItems.setVisibility(View.VISIBLE);
        }
        transferListAdapter.notifyDataSetChanged();
        hideKeyboard(this);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
