package com.futurearts.hiltonnewproj.activities.materialissue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.futurearts.hiltonnewproj.AppClass;
import com.futurearts.hiltonnewproj.BuildConfig;
import com.futurearts.hiltonnewproj.Constants;
import com.futurearts.hiltonnewproj.R;
import com.futurearts.hiltonnewproj.adapters.FactoryDataAdapter;
import com.futurearts.hiltonnewproj.adapters.TransferListAdapter;
import com.futurearts.hiltonnewproj.interfaces.CompletedListener;
import com.futurearts.hiltonnewproj.modelclasses.MaterialIssueDetails;
import com.futurearts.hiltonnewproj.models.productiondata.ProdInsert;
import com.futurearts.hiltonnewproj.utils.SharedPref;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransferListActivity extends AppCompatActivity implements CompletedListener {

    TextView tvNoItems;
    RecyclerView recyclerTransferList;
    ProgressBar progressBar;
    List<MaterialIssueDetails> materialIssueDetails;
    List<String> materialKeys;
    TransferListAdapter transferListAdapter;
    ImageView btnBack;

    Activity activity;
    SharedPref pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_list);
        activity = this;
        pref = new SharedPref(this);
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

        uploadToDB(materialIssue);
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

    private void uploadToDB(final MaterialIssueDetails productTable) {

        progressBar.setVisibility(View.VISIBLE);
        String URL;
        URL = Constants.BASE_URL + "material_issue.php";
        //System.out.println("CHECK---> URL " + URL);
        StringRequest jsonObjectRequestLogin;
        jsonObjectRequestLogin = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String jsonObject) {
                //System.out.println("CHECK---> Response " + jsonObject);
                progressBar.setVisibility(View.GONE);

//                Toast.makeText(activity, "Response--> "+jsonObject, Toast.LENGTH_SHORT).show();
                Log.e("material_issue.php",jsonObject);

                AppClass.getInstance().cancelPendingRequests("transfer_list");
                Gson gson = new Gson();
                ProdInsert prodInsert =gson.fromJson(jsonObject, ProdInsert.class);

                String errorCode = prodInsert.getErrorCode();
                String message = prodInsert.getMessage();
                Toast.makeText(TransferListActivity.this, message, Toast.LENGTH_SHORT).show();

                if (errorCode .equals( "0")) {

                    Toast.makeText(activity, "Data Inserted Successfully", Toast.LENGTH_SHORT).show();


                }else  if (errorCode .equals( "1")) {

                    Toast.makeText(activity, "Data Insertion Failed", Toast.LENGTH_SHORT).show();


                }



            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressBar.setVisibility(View.GONE);
                AppClass.getInstance().cancelPendingRequests("login");
                VolleyLog.d("Object Error : ", volleyError.getMessage());
                progressBar.setVisibility(View.GONE);
                Toast.makeText(TransferListActivity.this, volleyError.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //job_number, packOrEach, part_Num, qty_shortage, requiredLocation, urgent, who, where_to, job_image, where_from

                Log.e("job_number",productTable.getJob_Num());
                Log.e("packOrEach",productTable.getPackOrEach());
                Log.e("part_Num",productTable.getPart_Num());
                Log.e("qty_shortage",productTable.getQty_shortage()+"");
                Log.e("requiredLocation", String.valueOf(productTable.getRequiredLocation()));
                Log.e("urgent",String.valueOf(productTable.isUrgent()));
                Log.e("who", pref.getUserName());
                Log.e("where_to",productTable.getWhereTo());
                if(productTable.getMaterialJobImage()!=null){
                    Log.e("job_image",productTable.getMaterialJobImage());
                }else{
                    Log.e("job_image","");
                }
                Log.e("where_from",productTable.getWhereFrom());

                params.put("job_number",productTable.getJob_Num());
                params.put("packOrEach",productTable.getPackOrEach());
                params.put("part_Num",productTable.getPart_Num());
                params.put("qty_shortage",productTable.getQty_shortage()+"");
                params.put("requiredLocation", String.valueOf(productTable.getRequiredLocation()));
                params.put("urgent",String.valueOf(productTable.isUrgent()));
                params.put("who", pref.getUserName());
                params.put("where_to",productTable.getWhereTo());
                if(productTable.getMaterialJobImage()!=null){
                    params.put("job_image",productTable.getMaterialJobImage());
                }else{
                    params.put("job_image","");
                }

                params.put("where_from",productTable.getWhereFrom());



                return params;
            }
        };

        jsonObjectRequestLogin.setRetryPolicy(new DefaultRetryPolicy(50000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        RequestQueue requestQueue = Volley.newRequestQueue(this, commonSSLConnection.getHulkstack(CartListActivity.this));
        AppClass.getInstance().addToRequestQueue(jsonObjectRequestLogin, "login");
//        requestQueue.add(jsonObjectRequestLogin);


    }
}
