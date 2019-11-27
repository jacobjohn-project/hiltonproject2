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
import com.futurearts.hiltonnewproj.BuildConfig;
import com.futurearts.hiltonnewproj.R;
import com.futurearts.hiltonnewproj.activities.ScannerActivity;
import com.futurearts.hiltonnewproj.modelclasses.BatchContraolDetails;
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
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;


public class BatchControlActivity extends AppCompatActivity {


    LinearLayout mCameraLayout, btnScanOrderNo, btnScanPartNo;
    EditText etJobNumber, etPartNumber, etBatchNumber, etQty, etWorkcenter, etOperator;
    Button btnSubmit;
    ImageView imageView, btnBack, imgUndo;
    ProgressBar progressBar;
    String filePathNew = "", fileName = "";
    String jobNumber, partNumber, batchNumber, quantity;
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


                    BatchContraolDetails productTable = new BatchContraolDetails(etJobNumber.getText().toString(), etPartNumber.getText().toString(), etBatchNumber.getText().toString(), Integer.parseInt(etQty.getText().toString()),etWorkcenter.getText().toString(), DateUtils.getSystemDate(),pref.getUserName());
                    if (!fileName.equals("") && !filePathNew.equals("")) {
                        uploadImage(filePathNew, fileName, productTable);
                    }else{
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

    public void updateDb(BatchContraolDetails productTable) {
        mDatabase.push().setValue(productTable);

        fileName = "";
        filePathNew = "";
        jobNumber = "";
        partNumber = "";
        batchNumber = "";
        quantity = "";
//        locFrom = 0;
//        locTo = "";
//        movDate = "";
//        recDate = "";
//        orderNum = "";
//        signedBy = "";

        imageView.setImageDrawable(null);
        Toast.makeText(activity, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
        //pref.setLastUpdatedTime(System.currentTimeMillis());
        finish();

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

                etBatchNumber.requestFocus();
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
