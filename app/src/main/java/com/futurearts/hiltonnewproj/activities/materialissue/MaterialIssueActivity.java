package com.futurearts.hiltonnewproj.activities.materialissue;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.afollestad.materialcamera.MaterialCamera;
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
import com.futurearts.hiltonnewproj.activities.batchcontrol.BatchControlActivity;
import com.futurearts.hiltonnewproj.functions.SendMail;
import com.futurearts.hiltonnewproj.modelclasses.BatchContraolDetails;
import com.futurearts.hiltonnewproj.modelclasses.EmailDetails;
import com.futurearts.hiltonnewproj.modelclasses.MaterialIssueDetails;
import com.futurearts.hiltonnewproj.models.productiondata.ProdInsert;
import com.futurearts.hiltonnewproj.utils.DateUtils;
import com.futurearts.hiltonnewproj.utils.SharedPref;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MaterialIssueActivity extends AppCompatActivity {


    LinearLayout /*mCameraLayout,*/btnScanOrderNo, btnScanPartNo;
    EditText etSignedBy, etQtyShortage,etOrderNum, etPartNum;
    EditText editWhereTo,editWhereFrom;
    Button btnSubmit;
    ImageView imageView, btnBack;
    ProgressBar progressBar;
    String filePathNew = "", fileName = "";
    String signedBy, orderNum, locFrom, locTo, recDate, movDate;
    int partNum, qtyShortage;
    RadioGroup radioGroup, radioGrpPack;
    CheckBox checkBox;
    LinearLayout mCameraLayout;
    TextView resultTxt;

    Activity activity;
    Calendar myCalendar;
    public final static int MY_PERMISSIONS_REQUEST_READ_STORAGE = 100;
    private final static int CAMERA_RQ = 6969;
    DatabaseReference mDatabase;
    StorageReference mStorageRef;

    SharedPref pref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_issue);

        activity = this;

        initViews();

        myCalendar = Calendar.getInstance();

        mCameraLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckPermission();
            }
        });

        btnScanOrderNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MaterialIssueActivity.this, ScannerActivity.class);
                startActivityForResult(intent, 3);
            }
        });


        btnScanPartNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MaterialIssueActivity.this, ScannerActivity.class);
                startActivityForResult(intent, 4);
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



                signedBy = etSignedBy.getText().toString();
                locFrom = etQtyShortage.getText().toString();
                orderNum = etOrderNum.getText().toString();
                int selectedId = radioGroup.getCheckedRadioButtonId();
                int selectedPackOrEach = radioGrpPack.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                RadioButton radioSexButton = (RadioButton) radioGroup.findViewById(selectedId);
                String reqLoc = "";
                if (selectedId != -1) {
                    reqLoc = radioSexButton.getText().toString();
                }

                RadioButton radioPackOrEachButton = (RadioButton) radioGrpPack.findViewById(selectedPackOrEach);
                String reqPackorEach = "";
                if (selectedPackOrEach != -1) {
                    reqPackorEach = radioPackOrEachButton.getText().toString();
                }


                if (etPartNum.getText().toString().length() != 0 && etOrderNum.getText().toString().length() != 0) {
                    if (etQtyShortage.getText().toString().length() != 0) {
                        if (etSignedBy.getText().toString().trim().length() != 0) {
                            if (!reqPackorEach.equals("")) {
                                if (!reqLoc.equals("")) {
                                    MaterialIssueDetails productTable = new MaterialIssueDetails(etOrderNum.getText().toString(),
                                            etPartNum.getText().toString(),
                                            Integer.parseInt(etQtyShortage.getText().toString()),
                                            etSignedBy.getText().toString(), DateUtils.getSystemDate(),
                                            reqLoc, checkBox.isChecked(),reqPackorEach,editWhereFrom.getText().toString(),editWhereTo.getText().toString());
                                    if (!fileName.equals("") && !filePathNew.equals("")) {
                                        uploadImage(filePathNew, fileName, productTable);
                                    } else {
                                        checkDbforPartNumber(productTable);
                                    }
                                } else {
                                    Toast.makeText(activity, "Enter Required Location", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(activity, "Select Pack or Each", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(activity, "Enter Signed By", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(activity, "Enter Quantity Shortage", Toast.LENGTH_SHORT).show();
                    }
                } else if (etOrderNum.getText().toString().trim().length() != 0) {
                    if (etQtyShortage.getText().toString().length() != 0) {
                        if (etSignedBy.getText().toString().trim().length() != 0) {
                            if (!reqPackorEach.equals("")) {
                                if (!reqLoc.equals("")) {
                                    MaterialIssueDetails productTable = new MaterialIssueDetails(etOrderNum.getText().toString(),
                                            etPartNum.getText().toString(),
                                            Integer.parseInt(etQtyShortage.getText().toString()),
                                            etSignedBy.getText().toString(), DateUtils.getSystemDate(),
                                            reqLoc, checkBox.isChecked(),reqPackorEach,editWhereFrom.getText().toString(),editWhereTo.getText().toString());
                                    if (!fileName.equals("") && !filePathNew.equals("")) {
                                        uploadImage(filePathNew, fileName, productTable);
                                    } else {
                                        updateMaterialDb(productTable);
                                        uploadToDB(productTable);
                                    }

                                } else {
                                    Toast.makeText(activity, "Enter Required Location", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(activity, "Select Pack or Each", Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            Toast.makeText(activity, "Enter Signed By", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(activity, "Enter Quantity", Toast.LENGTH_SHORT).show();
                    }

                } else if (etPartNum.getText().toString().length() != 0) {
                    if (etQtyShortage.getText().toString().length() != 0) {
                        if (etSignedBy.getText().toString().trim().length() != 0) {
                            if (!reqPackorEach.equals("")) {
                                if (!reqLoc.equals("")) {
                                    MaterialIssueDetails productTable = new MaterialIssueDetails(etOrderNum.getText().toString(),
                                            etPartNum.getText().toString(),
                                            Integer.parseInt(etQtyShortage.getText().toString()),
                                            etSignedBy.getText().toString(), DateUtils.getSystemDate(),
                                            reqLoc, checkBox.isChecked(),reqPackorEach,editWhereFrom.getText().toString(),editWhereTo.getText().toString());
                                    if (!fileName.equals("") && !filePathNew.equals("")) {
                                        uploadImage(filePathNew, fileName, productTable);
                                    } else {
                                        checkDbforPartNumber(productTable);
                                    }
                                } else {
                                    Toast.makeText(activity, "Enter Required Location", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(activity, "Select Pack or Each", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(activity, "Enter Signed By", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(activity, "Enter Quantity", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(activity, "Enter Job Number/Part Number", Toast.LENGTH_SHORT).show();
                }


            }
        });


        radioGroup.clearCheck();
        radioGrpPack.clearCheck();


    }

    private void clearAll() {

//        resultTxt.setText("");
        etOrderNum.setText("");
        etQtyShortage.setText("");
        etQtyShortage.setText("");
        editWhereFrom.setText("");
        editWhereTo.setText("");
        etPartNum.setText("");
        radioGroup.clearCheck();
        radioGrpPack.clearCheck();
        checkBox.setChecked(false);
        fileName = "";
        filePathNew = "";
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_camera));
        fileName = "";
        filePathNew = "";
        locFrom = "";
        locTo = "";
        movDate = "";
        recDate = "";
        orderNum = "";
        signedBy = "";

    }


    private void initViews() {


        mCameraLayout = findViewById(R.id.linearLayout);
        btnScanOrderNo = findViewById(R.id.btnScanOrderNo);
        btnScanPartNo = findViewById(R.id.btnScanPartNo);
        etOrderNum = findViewById(R.id.etOrderNo);
        etSignedBy = findViewById(R.id.orderNo8);
        etQtyShortage = findViewById(R.id.orderNo7);
        etPartNum = findViewById(R.id.partNumber);
        btnSubmit = findViewById(R.id.button2);
        imageView = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progressBar);
        btnBack = findViewById(R.id.btnBack);
        radioGroup = findViewById(R.id.radioGrpLoc);
        radioGrpPack = findViewById(R.id.radioGrpPack);
        checkBox = findViewById(R.id.checkBox);
        resultTxt = findViewById(R.id.resultTxt);
        editWhereFrom = findViewById(R.id.editWhereFrom);
        editWhereTo=findViewById(R.id.editWhereTo);



       /* recieveDate.setFocusable(false);
        recieveDate.setClickable(true);
        movingDate.setFocusable(false);
        movingDate.setClickable(true);
*/

        pref = new SharedPref(this);

        etSignedBy.setText("Signed By: " + pref.getUserName());
        etOrderNum.requestFocus();
        //pref.setLastUpdatedTime(System.currentTimeMillis());

        mDatabase = FirebaseDatabase.getInstance().getReference().child(BuildConfig.BASE_TABLE).child(BuildConfig.MATERIAL_ISSUE_TABLE);
        mStorageRef = FirebaseStorage.getInstance().getReference().child(BuildConfig.BASE_TABLE).child(BuildConfig.MATERIAL_ISSUE_TABLE);

    }



    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MaterialIssueActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {

                    File mFile = new File(getExternalFilesDir(null), "uploads");
                    if (!mFile.exists())
                        if (!mFile.mkdir())
                            throw new RuntimeException("Unable to create save directory, make sure WRITE_EXTERNAL_STORAGE permission is granted.");

                    new MaterialCamera(MaterialIssueActivity.this)
                            /** all the previous methods can be called, but video ones would be ignored */
                            .saveDir(mFile)
                            .primaryColorAttr(R.attr.colorPrimary)
                            .defaultToFrontFacing(false)
                            .stillShot() // launches the Camera in stillshot mode
                            .start(CAMERA_RQ);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    public void uploadImage(String filePath, String fileName, final MaterialIssueDetails productTable) {

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);


            final StorageReference storageRef = mStorageRef.child(fileName);
            storageRef.putFile(Uri.parse(filePath))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();

                            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Log.d("DOWNLOAD PATH", "onSuccess: uri= " + uri.toString());
                                    String outputurl = uri.toString();
                                    productTable.setMaterialJobImage(outputurl);
                                    if (!productTable.getPart_Num().equals("")) {
                                        checkDbforPartNumber(productTable);
                                    } else {
                                        updateMaterialDb(productTable);
                                        uploadToDB(productTable);
                                    }

                                }
                            });


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            //you can display an error toast
        }

    }


    public void updateMaterialDb(MaterialIssueDetails productTable) {

        mDatabase.push().setValue(productTable);


//        imageView.setImageDrawable(null);
//        Toast.makeText(activity, "Job Number Successfully Uploaded at "+productTable.saved_date, Toast.LENGTH_LONG).show();
        //resultTxt.setText("Job Number Successfully Uploaded at " + productTable.saved_date);
        //pref.setLastUpdatedTime(System.currentTimeMillis());
//        clearAll();
        //finish();
    }

    private void uploadToDB(final MaterialIssueDetails productTable) {

        progressBar.setVisibility(View.VISIBLE);
        String URL;
        URL = Constants.BASE_URL + "material_issue.php";
        //System.out.println("CHECK---> URL " + URL);
        StringRequest jsonObjectRequestLogin = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String jsonObject) {
                //System.out.println("CHECK---> Response " + jsonObject);
                progressBar.setVisibility(View.GONE);

//                Toast.makeText(activity, "Response--> "+jsonObject, Toast.LENGTH_SHORT).show();
                Log.e("material_issue.php",jsonObject);

                AppClass.getInstance().cancelPendingRequests("insert_prod");
                Gson gson = new Gson();
                ProdInsert prodInsert =gson.fromJson(jsonObject, ProdInsert.class);

                String errorCode = prodInsert.getErrorCode();
                String message = prodInsert.getMessage();
                Toast.makeText(MaterialIssueActivity.this, message, Toast.LENGTH_SHORT).show();

                if (errorCode .equals( "0")) {

                    Toast.makeText(activity, "Data Inserted Successfully", Toast.LENGTH_SHORT).show();
                    resultTxt.setText("Job Number Successfully Uploaded at " + productTable.saved_date);
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
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MaterialIssueActivity.this, volleyError.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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

    public void updateMailJobDb(MaterialIssueDetails productTable) {
        DatabaseReference mailJobDb = FirebaseDatabase.getInstance().getReference().child(BuildConfig.BASE_TABLE).child(BuildConfig.EMAIL_JOBS_TABLE);
        mailJobDb.push().setValue(productTable);

        fileName = "";
        filePathNew = "";
        locFrom = "";
        locTo = "";
        movDate = "";
        recDate = "";
        orderNum = "";
        signedBy = "";

//        imageView.setImageDrawable(null);
//        Toast.makeText(activity, "Job Added to Mail Jobs", Toast.LENGTH_LONG).show();
        resultTxt.setText("Job Added to Mail Jobs");
        //pref.setLastUpdatedTime(System.currentTimeMillis());
//        clearAll();
        //finish();
    }

    public void checkDbforPartNumber(final MaterialIssueDetails productTable) {
        DatabaseReference mMaiDatabase = FirebaseDatabase.getInstance().getReference().child(BuildConfig.BASE_TABLE).child(BuildConfig.EMAIL_TABLE);
        mMaiDatabase.keepSynced(true);
        mMaiDatabase.orderByChild("part_number").equalTo(productTable.getPart_Num()).limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot post : dataSnapshot.getChildren()) {
                        EmailDetails emailDetails = post.getValue(EmailDetails.class);
                        productTable.setEmailed_To(emailDetails.getEmail_id());
                        sendMailtoEmailId(productTable, emailDetails.getEmail_id());
                    }
                } else {
                    updateMaterialDb(productTable);
                    uploadToDB(productTable);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void sendMailtoEmailId(MaterialIssueDetails materialIssueDetails, String emailId) {
        SendMail sm = new SendMail(MaterialIssueActivity.this, emailId, "MATERIAL REQUEST", emailContent(materialIssueDetails));
        sm.execute();
        updateMailJobDb(materialIssueDetails);
    }


    public String emailContent(MaterialIssueDetails materialIssueDetails) {
        return "Job Number:" + materialIssueDetails.getJob_Num() + "\n" +
                "Part Number:" + materialIssueDetails.getPart_Num() + "\n" +
                "Quantity:" + materialIssueDetails.getQty_shortage() + "\n" +
                "Pack/Each:" + materialIssueDetails.getPackOrEach() + "\n" +
                "Request Location:" + materialIssueDetails.getRequiredLocation() + "\n" +
                "User:" + materialIssueDetails.getWho() + "\n" +
                "\n" +
                "Kindly do not reply to this email for the above mentioned matter. Thanks";
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        // This is important, otherwise the result will not be passed to the fragment
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_RQ) {


                Picasso.get().load(data.getDataString()).resize(120, 120).centerCrop().placeholder(R.drawable.ic_camera).into(imageView);

//                fileName = "pic.jp";
                filePathNew = data.getDataString();
                fileName = filePathNew.substring(filePathNew.lastIndexOf("/") + 1);

            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                File newFile = new File(picturePath);
                fileName = newFile.getName();
                filePathNew = selectedImage.toString();
                Picasso.get().load(selectedImage).resize(120, 120).centerCrop().placeholder(R.drawable.ic_camera).into(imageView);


            } else if (requestCode == 3) {
                String message = data.getStringExtra("MESSAGE");
                etOrderNum.setText(message);

                etPartNum.requestFocus();
            } else if (requestCode == 4) {
                String message = data.getStringExtra("MESSAGE");
                etPartNum.setText(message);

                etQtyShortage.requestFocus();
            }
        }


    }


    public void saveFile(Bitmap bmp) {
        try {


            String path = Environment.getExternalStorageDirectory().toString();
            OutputStream fOut = null;
            Integer counter = 0;
            //System.out.println("Chillarcat: "+path);
            fileName = String.valueOf(System.currentTimeMillis()) + ".jpg";
            File file = new File(path, fileName);
            filePathNew = file.getAbsolutePath();
            fOut = new FileOutputStream(file);


            Bitmap pictureBitmap = bmp; // obtaining the Bitmap
            pictureBitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
            fOut.flush(); // Not really required
            fOut.close(); // do not forget to close the stream

            MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void CheckPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            if (
                    ActivityCompat.shouldShowRequestPermissionRationale(MaterialIssueActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                            ActivityCompat.shouldShowRequestPermissionRationale(MaterialIssueActivity.this,
                                    Manifest.permission.READ_EXTERNAL_STORAGE) &&
                            ActivityCompat.shouldShowRequestPermissionRationale(MaterialIssueActivity.this,
                                    Manifest.permission.CAMERA)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                ActivityCompat.requestPermissions(MaterialIssueActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_READ_STORAGE);
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(MaterialIssueActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_READ_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            selectImage();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    // dispatchTakePictureIntent();

                    try {
                        selectImage();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
}
