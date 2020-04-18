package com.futurearts.hiltonnewproj.activities.batchcontrol;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.futurearts.hiltonnewproj.activities.ScannerActivity;
import com.futurearts.hiltonnewproj.modelclasses.BatchContraolDetails;
import com.futurearts.hiltonnewproj.modelclasses.EmailDetails;
import com.futurearts.hiltonnewproj.modelclasses.ReportIssueDetails;
import com.futurearts.hiltonnewproj.models.productiondata.ProdInsert;
import com.futurearts.hiltonnewproj.utils.DateUtils;
import com.futurearts.hiltonnewproj.utils.SharedPref;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class ReportIssueActivity extends AppCompatActivity {

    LinearLayout mCameraLayout, btnScanOrderNo, btnScanPartNo, btnScanWorkCenter, btnScanOperator;
    EditText etJobNumber, etPartNumber, etQty, etWorkcenter, etOperator, etNotes;
    Button btnSubmit;
    ImageView btnBack, imgUndo;
    ProgressBar progressBar;
    String jobNumber, partNumber, batchNumber, quantity;
    String issue = "";
    RadioGroup radioGrpPack;
//    int locFrom;

    Activity activity;
    public final static int MY_PERMISSIONS_REQUEST_READ_STORAGE = 100;
    private final static int CAMERA_RQ = 6969;
    DatabaseReference mDatabase;
    DatabaseReference mDatabaseProductionData;
    SharedPref pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_issue);
        initViews();
        btnScanOrderNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ReportIssueActivity.this, ScannerActivity.class);
                startActivityForResult(intent, 3);
            }
        });

        btnScanPartNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ReportIssueActivity.this, ScannerActivity.class);
                startActivityForResult(intent, 4);
            }
        });

        btnScanWorkCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ReportIssueActivity.this, ScannerActivity.class);
                startActivityForResult(intent, 5);
            }
        });

        btnScanOperator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ReportIssueActivity.this, ScannerActivity.class);
                startActivityForResult(intent, 6);
            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isValidationFailed()) {
                    progressBar.setVisibility(View.VISIBLE);
                    String notes = "";
                    if (etNotes.getText().toString().length() > 0) {
                        notes = etNotes.getText().toString();
                    }
                    int selectedPackOrEach = radioGrpPack.getCheckedRadioButtonId();
                    RadioButton radioPackOrEachButton = (RadioButton) radioGrpPack.findViewById(selectedPackOrEach);
                    issue = "";
                    if (selectedPackOrEach != -1) {
                        issue = radioPackOrEachButton.getText().toString();
                    }

                    String job_part_work_operator=etJobNumber.getText().toString()+"_"+etPartNumber.getText().toString()+"_"+etWorkcenter.getText().toString()+"_"+etOperator.getText().toString();
                    ReportIssueDetails reportIssueDetails = new ReportIssueDetails(etJobNumber.getText().toString(), etPartNumber.getText().toString(),etWorkcenter.getText().toString(),
                            etOperator.getText().toString(),Integer.parseInt(etQty.getText().toString()),pref.getUserName(),DateUtils.getSystemDate(),issue,notes);

                    addtoIssueDb(reportIssueDetails);
                    addtoServerDb(reportIssueDetails);
                    addtoProductionDataIfMatches(job_part_work_operator,issue,notes);
                }


            }
        });

        imgUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clearAll();

            }
        });
    }

    public void addtoIssueDb(ReportIssueDetails reportIssueDetails){
        mDatabase.push().setValue(reportIssueDetails);
    }

    public void addtoProductDb(BatchContraolDetails batchContraolDetails,String key){
        mDatabaseProductionData.child(key).setValue(batchContraolDetails);
    }

    public void addtoServerDb(final ReportIssueDetails reportIssueDetails){
        String URL;
        URL = Constants.BASE_URL + "report_issue.php";
        //System.out.println("CHECK---> URL " + URL);
        StringRequest jsonObjectRequestLogin = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String jsonObject) {
                //System.out.println("CHECK---> Response " + jsonObject);
                progressBar.setVisibility(View.GONE);

//                Toast.makeText(activity, "Response--> "+jsonObject, Toast.LENGTH_SHORT).show();
                Log.e("production_insert.php",jsonObject);

                AppClass.getInstance().cancelPendingRequests("insert_prod");
                Gson gson = new Gson();
                ProdInsert prodInsert =gson.fromJson(jsonObject, ProdInsert.class);

                String errorCode = prodInsert.getErrorCode();
                String message = prodInsert.getMessage();
                Toast.makeText(ReportIssueActivity.this, message, Toast.LENGTH_SHORT).show();

                if (errorCode .equals( "0")) {

                    Toast.makeText(activity, "Data Inserted Successfully", Toast.LENGTH_SHORT).show();
                    clearAll();

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

                Toast.makeText(ReportIssueActivity.this, volleyError.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                Log.e("job_or_chassis_number",reportIssueDetails.getJob_number());
                Log.e("part_number",reportIssueDetails.getPart_number());
                Log.e("work_center",reportIssueDetails.getWork_center());
                Log.e("operator",reportIssueDetails.getOperator());
                Log.e("quantity", String.valueOf(reportIssueDetails.getQuantity()));
                Log.e("issue", reportIssueDetails.getIssue());
                Log.e("added_by", pref.getUserName());
                Log.e("text",reportIssueDetails.getNotes());

                params.put("job_or_chassis_number", reportIssueDetails.getJob_number());
                params.put("part_number", reportIssueDetails.getPart_number());
                params.put("work_center", reportIssueDetails.getWork_center());
                params.put("operator", reportIssueDetails.getOperator());
                params.put("quantity", String.valueOf(reportIssueDetails.getQuantity()));
                params.put("issue", reportIssueDetails.getIssue());
                params.put("added_by", pref.getUserName());
                params.put("text",reportIssueDetails.getNotes());

                return params;
            }
        };

        jsonObjectRequestLogin.setRetryPolicy(new DefaultRetryPolicy(50000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        RequestQueue requestQueue = Volley.newRequestQueue(this, commonSSLConnection.getHulkstack(CartListActivity.this));
        AppClass.getInstance().addToRequestQueue(jsonObjectRequestLogin, "login");
//        requestQueue.add(jsonObjectRequestLogin);
    }

    public void addtoProductionDataIfMatches(String jobPartWorkOperator, final String issue, final String newNotes){
        mDatabaseProductionData.keepSynced(true);
        mDatabaseProductionData.orderByChild("job_part_work_operator").equalTo(jobPartWorkOperator).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot post : dataSnapshot.getChildren()) {
                        BatchContraolDetails batchContraolDetails = post.getValue(BatchContraolDetails.class);
                        batchContraolDetails.setIssue(issue);
                        batchContraolDetails.appendNote(newNotes);
                        addtoProductDb(batchContraolDetails,post.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean isValidationFailed() {
        boolean failFlag = false;
        if (etJobNumber.getText().toString().length() == 0) {
            failFlag = true;
            Toast.makeText(activity, "Enter Job/Chassis Number", Toast.LENGTH_SHORT).show();
        } else if (etPartNumber.getText().toString().length() == 0) {
            failFlag = true;
            Toast.makeText(activity, "Enter Part Number", Toast.LENGTH_SHORT).show();
        } else if (etWorkcenter.getText().toString().length() == 0) {
            failFlag = true;
            Toast.makeText(activity, "Enter Work Center", Toast.LENGTH_SHORT).show();
        } else if (etOperator.getText().toString().length() == 0) {
            failFlag = true;
            Toast.makeText(activity, "Enter Operator", Toast.LENGTH_SHORT).show();
        } else if (etQty.getText().toString().length() == 0) {
            failFlag = true;
            Toast.makeText(activity, "Enter Quantity", Toast.LENGTH_SHORT).show();
        } else if (radioGrpPack.getCheckedRadioButtonId() == -1) {
            failFlag = true;
            Toast.makeText(activity, "Select Issue", Toast.LENGTH_SHORT).show();
        }


        return failFlag;
    }

    private void clearAll() {
        etJobNumber.setText("");
        etPartNumber.setText("");
        etWorkcenter.setText("");
        etOperator.setText("");
        etQty.setText("");
        etNotes.setText("");
        issue = "";
        radioGrpPack.clearCheck();
        jobNumber = "";
        partNumber = "";
        batchNumber = "";
        quantity = "";
    }

    private void initViews() {
        activity = ReportIssueActivity.this;
        mCameraLayout = findViewById(R.id.linearLayout);
        btnScanOrderNo = findViewById(R.id.btnScanOrderNo);
        btnScanPartNo = findViewById(R.id.btnScanPartNo);
        btnScanWorkCenter = findViewById(R.id.btnScanWorkCenter);
        btnScanOperator = findViewById(R.id.btnScanOperator);
        etJobNumber = findViewById(R.id.etJobNumber);
        etPartNumber = findViewById(R.id.etPartNumber);
        etWorkcenter = findViewById(R.id.etWorkcenter);
        etQty = findViewById(R.id.etQuantity);
        btnSubmit = findViewById(R.id.btnSubmit);
        progressBar = findViewById(R.id.progressBar);
        btnBack = findViewById(R.id.btnBack);
        imgUndo = findViewById(R.id.imgUndo);
        etOperator = findViewById(R.id.etOperator);
        radioGrpPack = findViewById(R.id.radioGrpIssue);
        etNotes = findViewById(R.id.etNotes);
        pref = new SharedPref(this);
        etJobNumber.requestFocus();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(BuildConfig.BASE_TABLE).child(BuildConfig.REPORT_ISSUE_TABLE);
        mDatabaseProductionData=FirebaseDatabase.getInstance().getReference().child(BuildConfig.BASE_TABLE).child(BuildConfig.BATCH_CONTROL_TABLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 3) {
                String message = data.getStringExtra("MESSAGE");
                etJobNumber.setText(message);
                etPartNumber.requestFocus();
            } else if (requestCode == 4) {
                String message = data.getStringExtra("MESSAGE");
                etPartNumber.setText(message);
                etWorkcenter.requestFocus();
            } else if (requestCode == 5) {
                String message = data.getStringExtra("MESSAGE");
                etWorkcenter.setText(message);
                etOperator.requestFocus();
            } else if (requestCode == 6) {
                String message = data.getStringExtra("MESSAGE");
                etOperator.setText(message);
                etQty.requestFocus();
            }
        }


    }
}
