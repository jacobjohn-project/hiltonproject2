package com.futurearts.hiltonnewproj.activities.batchcontrol;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.futurearts.hiltonnewproj.activities.LoginActivity;
import com.futurearts.hiltonnewproj.activities.MainActivity;
import com.futurearts.hiltonnewproj.activities.ScannerActivity;
import com.futurearts.hiltonnewproj.modelclasses.BatchContraolDetails;
import com.futurearts.hiltonnewproj.models.login.Loginapi;
import com.futurearts.hiltonnewproj.models.productiondata.Data;
import com.futurearts.hiltonnewproj.models.productiondata.ProdInsert;
import com.futurearts.hiltonnewproj.utils.DateUtils;
import com.futurearts.hiltonnewproj.utils.SharedPref;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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


public class BatchControlActivity extends AppCompatActivity {


    LinearLayout mCameraLayout,mTypeLayout, btnScanOrderNo, btnScanPartNo,btnScanWorkCenter,btnScanOperator,btnScanBatch,btnPOnum;
    EditText etJobNumber, etPartNumber, etBatchNumber, etQty, etWorkcenter, etOperator,etPOnumber;
    Button btnSubmit;
    ImageView imageView, btnBack, imgUndo;
    ProgressBar progressBar;
    String filePathNew = "", fileName = "";
    String jobNumber, partNumber, batchNumber, quantity, type="";
//    int locFrom;

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
        setContentView(R.layout.activity_batch_control);

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

                Intent intent = new Intent(BatchControlActivity.this, ScannerActivity.class);
                startActivityForResult(intent, 3);
            }
        });

        btnScanPartNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(BatchControlActivity.this, ScannerActivity.class);
                startActivityForResult(intent, 4);
            }
        });

        btnScanWorkCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(BatchControlActivity.this, ScannerActivity.class);
                startActivityForResult(intent, 5);
            }
        });

        btnScanOperator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(BatchControlActivity.this, ScannerActivity.class);
                startActivityForResult(intent, 6);
            }
        });

        btnScanBatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(BatchControlActivity.this, ScannerActivity.class);
                startActivityForResult(intent, 7);
            }
        });

        btnPOnum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(BatchControlActivity.this, ScannerActivity.class);
                startActivityForResult(intent, 8);
            }
        });


        mTypeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(BatchControlActivity.this, ScannerActivity.class);
                startActivityForResult(intent, 9);
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

                progressBar.setVisibility(View.VISIBLE);
                if (!isValidationFailed()) {


                    BatchContraolDetails productTable = new BatchContraolDetails(etJobNumber.getText().toString(), etPartNumber.getText().toString(), etBatchNumber.getText().toString(), Integer.parseInt(etQty.getText().toString()),etWorkcenter.getText().toString(), DateUtils.getSystemDate(),pref.getUserName(),etOperator.getText().toString(),etPOnumber.getText().toString());
                    if (!fileName.equals("") && !filePathNew.equals("")) {
                        uploadImage(filePathNew, fileName, productTable);
                    }else{

                        uploadToDB(productTable);
                        updateDb(productTable);
                    }

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

    private void clearAll() {


        etJobNumber.setText("");
        etPartNumber.setText("");
        etWorkcenter.setText("");
        etBatchNumber.setText("");
        etOperator.setText("");
        etPOnumber.setText("");
        etQty.setText("");
        fileName = "";
        filePathNew = "";
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_camera));

    }

    private boolean isValidationFailed() {
        boolean failFlag = false;
        if (etJobNumber.getText().toString().length() == 0) {
            failFlag = true;
            Toast.makeText(activity, "Enter Job/Chassis Number", Toast.LENGTH_SHORT).show();
        }/* else if (etBatchNumber.getText().toString().length() == 0) {
            failFlag = true;
            Toast.makeText(activity, "Enter Batch Number", Toast.LENGTH_SHORT).show();
        }*/ else if (etQty.getText().toString().length() == 0) {
//            failFlag = true;
//            Toast.makeText(activity, "Enter Quantity", Toast.LENGTH_SHORT).show();
            etQty.setText("1");
        }/* else if (etWorkcenter.getText().toString().length() == 0) {
            failFlag = true;
            Toast.makeText(activity, "Enter Work Center", Toast.LENGTH_SHORT).show();
        }else if (etOperator.getText().toString().length() == 0) {
            failFlag = true;
            Toast.makeText(activity, "Enter Operator", Toast.LENGTH_SHORT).show();
        }*/ else if (etQty.getText().toString().length() != 0) {
            try {
                int num = Integer.parseInt(etQty.getText().toString());
            } catch (NumberFormatException e) {
                e.printStackTrace();
//                failFlag = true;
//                Toast.makeText(activity, "Enter valid Quantity", Toast.LENGTH_SHORT).show();
                etQty.setText("1");
            }
//            if (!failFlag) {
//                if (fileName.equals("") && filePathNew.equals("")) {
//                    failFlag = true;
//                    Toast.makeText(activity, "Add image", Toast.LENGTH_SHORT).show();
//                }
//            }
        }


        return failFlag;
    }

    private void initViews() {


        mCameraLayout = findViewById(R.id.linearLayout);
        btnScanOrderNo = findViewById(R.id.btnScanOrderNo);
        btnScanPartNo = findViewById(R.id.btnScanPartNo);
        btnScanWorkCenter = findViewById(R.id.btnScanWorkCenter);
        btnScanBatch = findViewById(R.id.btnScanBatch);
        btnScanOperator = findViewById(R.id.btnScanOperator);
        btnPOnum = findViewById(R.id.btnPOnum);
        etJobNumber = findViewById(R.id.etJobNumber);
        etPartNumber = findViewById(R.id.etPartNumber);
        etBatchNumber = findViewById(R.id.etBatchNumber);
        etWorkcenter = findViewById(R.id.etWorkcenter);
        etQty = findViewById(R.id.etQuantity);
        btnSubmit = findViewById(R.id.btnSubmit);
        imageView = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progressBar);
        btnBack = findViewById(R.id.btnBack);
        imgUndo = findViewById(R.id.imgUndo);
        etOperator = findViewById(R.id.etOperator);
        etPOnumber = findViewById(R.id.etPOnumber);
        mTypeLayout = findViewById(R.id.linearLayout1);



        pref = new SharedPref(this);

//        etSignedBy.setText("Signed By: "+pref.getUserName());
        etJobNumber.requestFocus();
        //pref.setLastUpdatedTime(System.currentTimeMillis());

        mDatabase = FirebaseDatabase.getInstance().getReference().child(BuildConfig.BASE_TABLE).child(BuildConfig.BATCH_CONTROL_TABLE);
        mStorageRef = FirebaseStorage.getInstance().getReference().child(BuildConfig.BASE_TABLE).child(BuildConfig.BATCH_CONTROL_TABLE);

    }

   /* private void updateLabel1() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        recieveDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabel2() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        movingDate.setText(sdf.format(myCalendar.getTime()));
    }*/

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(BatchControlActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    //Default android camera

                    /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);*/

                    //Custom Camera

                    /*Intent i = new Intent(BatchControlActivity.this,CameraActivity.class);
                    startActivity(i);*/

                    //Material Camera Library

                    File mFile = new File(getExternalFilesDir(null), "uploads");
                    if (!mFile.exists())
                        if (!mFile.mkdir())
                            throw new RuntimeException("Unable to create save directory, make sure WRITE_EXTERNAL_STORAGE permission is granted.");

                    new MaterialCamera(BatchControlActivity.this)
                            /** all the previous methods can be called, but video ones would be ignored */
                            .saveDir(mFile)
                            .primaryColorAttr(R.attr.colorPrimary)
                            .defaultToFrontFacing(false)
                            .stillShot() // launches the Camera in stillshot mode
                            .start(CAMERA_RQ);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void uploadImage(String filePath, String fileName, final BatchContraolDetails productTable) {

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
                                    productTable.setImage_url(outputurl);
                                    uploadToDB(productTable);
                                    updateDb(productTable);
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

    private void uploadToDB(final BatchContraolDetails productTable) {

        String URL;
        URL = Constants.BASE_URL + "production_insert.php";
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
                Toast.makeText(BatchControlActivity.this, message, Toast.LENGTH_SHORT).show();

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

                Toast.makeText(BatchControlActivity.this, volleyError.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                Log.e("job_or_chassis_number",productTable.getJob_number());
                Log.e("part_number",productTable.getPart_number());
                Log.e("work_center",productTable.getWork_center());
                Log.e("operator",productTable.getOperator());
                Log.e("quantity", String.valueOf(productTable.getQuantity()));
                Log.e("batch_number",productTable.getBatch_number());
                Log.e("po_number",productTable.getPo_number());
                Log.e("added_by",pref.getUserName());
                if(!type.equals("")) {
                    Log.e("type", type);
                }
                if(productTable.getImage_url()!=null){
                    Log.e("image_url",productTable.getImage_url());
                }


                params.put("job_or_chassis_number", productTable.getJob_number());
                params.put("part_number", productTable.getPart_number());
                params.put("work_center", productTable.getWork_center());
                params.put("operator", productTable.getOperator());
                params.put("quantity", String.valueOf(productTable.getQuantity()));
                params.put("batch_number", productTable.getBatch_number());
                params.put("po_number", productTable.getPo_number());
                params.put("added_by", pref.getUserName());
                params.put("type", type);
                if(productTable.getImage_url()!=null){
                    params.put("image_url", productTable.getImage_url());
                }else{
                    params.put("image_url", "");
                }

                //System.out.println("CHECK---> " + prefManager.getStudentUserId() + " , " +
//                        prefManager.getSChoolID()+ " ,\n " +realOrderID);

                return params;
            }
        };

        jsonObjectRequestLogin.setRetryPolicy(new DefaultRetryPolicy(50000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        RequestQueue requestQueue = Volley.newRequestQueue(this, commonSSLConnection.getHulkstack(CartListActivity.this));
        AppClass.getInstance().addToRequestQueue(jsonObjectRequestLogin, "login");
//        requestQueue.add(jsonObjectRequestLogin);


    }

    public void updateDb(BatchContraolDetails productTable) {
        mDatabase.push().setValue(productTable);

        fileName = "";
        filePathNew = "";
        jobNumber = "";
        partNumber = "";
        batchNumber = "";
        quantity = "";
        type="";
//        locFrom = 0;
//        locTo = "";
//        movDate = "";
//        recDate = "";
//        orderNum = "";
//        signedBy = "";

        imageView.setImageDrawable(null);
        clearAll();
//        Toast.makeText(activity, "Job number Successfully Uploaded at "+productTable.getDate_time() , Toast.LENGTH_LONG).show();
        //pref.setLastUpdatedTime(System.currentTimeMillis());
//        finish();

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
/*                    Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                    Log.w("HiltonLog", picturePath+"");
                    imageView.setImageBitmap(thumbnail);   */
                Picasso.get().load(selectedImage).resize(120, 120).centerCrop().placeholder(R.drawable.ic_camera).into(imageView);


            } else if (requestCode == 3) {
                String message = data.getStringExtra("MESSAGE");
                etJobNumber.setText(message);

                etPartNumber.requestFocus();
            } else if (requestCode == 4) {
                String message = data.getStringExtra("MESSAGE");
                etPartNumber.setText(message);

                etWorkcenter.requestFocus();
            }else if (requestCode == 5) {
                String message = data.getStringExtra("MESSAGE");
                etWorkcenter.setText(message);

                etOperator.requestFocus();
            }else if (requestCode == 6) {
                String message = data.getStringExtra("MESSAGE");
                etOperator.setText(message);

                etQty.requestFocus();
            }else if (requestCode == 7) {
                String message = data.getStringExtra("MESSAGE");
                etBatchNumber.setText(message);

                etPOnumber.requestFocus();
            }else if (requestCode == 8) {
                String message = data.getStringExtra("MESSAGE");
                etPOnumber.setText(message);

            }else if (requestCode == 9) {
                String message = data.getStringExtra("MESSAGE");

                type = message;
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
                    ActivityCompat.shouldShowRequestPermissionRationale(BatchControlActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                            ActivityCompat.shouldShowRequestPermissionRationale(BatchControlActivity.this,
                                    Manifest.permission.READ_EXTERNAL_STORAGE) &&
                            ActivityCompat.shouldShowRequestPermissionRationale(BatchControlActivity.this,
                                    Manifest.permission.CAMERA)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                ActivityCompat.requestPermissions(BatchControlActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_READ_STORAGE);
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(BatchControlActivity.this,
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
