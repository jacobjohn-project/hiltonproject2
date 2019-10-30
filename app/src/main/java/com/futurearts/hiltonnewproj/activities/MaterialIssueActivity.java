package com.futurearts.hiltonnewproj.activities;


import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.afollestad.materialcamera.MaterialCamera;
import com.futurearts.hiltonnewproj.BuildConfig;
import com.futurearts.hiltonnewproj.R;
import com.futurearts.hiltonnewproj.modelclasses.MaterialIssueDetails;
import com.futurearts.hiltonnewproj.utils.DateUtils;
import com.futurearts.hiltonnewproj.utils.SharedPref;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

public class MaterialIssueActivity extends AppCompatActivity {


    LinearLayout /*mCameraLayout,*/btnScanOrderNo;
    EditText etSignedBy, etQtyShortage,/*etLocTo,*/
            etPartName, etOrderNum, etPartNum/*,recieveDate,movingDate*/;
    Button btnSubmit;
    ImageView imageView, btnBack;
    ProgressBar progressBar;
    String filePathNew = "", fileName = "";
    String signedBy, orderNum, locFrom, locTo, recDate, movDate;
    int partNum, qtyShortage;
    RadioGroup radioGroup;

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


        btnScanOrderNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MaterialIssueActivity.this, ScannerActivity.class);
                startActivityForResult(intent, 3);
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

                // find the radiobutton by returned id
                RadioButton radioSexButton = (RadioButton) findViewById(selectedId);
                String reqLoc = radioSexButton.getText().toString();

                if (etOrderNum.getText().toString().trim().length() != 0) {
                    if (etQtyShortage.getText().toString().length() != 0) {
                        if (etSignedBy.getText().toString().trim().length() != 0) {
                            if(!reqLoc.equals("")){
                                MaterialIssueDetails productTable = new MaterialIssueDetails(etOrderNum.getText().toString(),
                                        Integer.parseInt(etPartNum.getText().toString()),
                                        Integer.parseInt(etQtyShortage.getText().toString()),
                                        etSignedBy.getText().toString(), DateUtils.getSystemDate());
                                updateDb(productTable);
                            }else{
                                Toast.makeText(activity, "Enter Required Location", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(activity, "Enter Signed By", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(activity, "Enter Quantity Shortage", Toast.LENGTH_SHORT).show();
                    }

                } else if (etPartNum.getText().toString().length() != 0) {
                    if (etQtyShortage.getText().toString().length() != 0) {
                        if (etSignedBy.getText().toString().trim().length() != 0) {
                            if(!reqLoc.equals("")){
                                MaterialIssueDetails productTable = new MaterialIssueDetails(etOrderNum.getText().toString(),
                                        Integer.parseInt(etPartNum.getText().toString()),
                                        Integer.parseInt(etQtyShortage.getText().toString()),
                                        etSignedBy.getText().toString(), DateUtils.getSystemDate());
                                updateDb(productTable);
                            }else{
                                Toast.makeText(activity, "Enter Required Location", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(activity, "Enter Signed By", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(activity, "Enter Quantity Shortage", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(activity, "Enter Job Number/Part Number", Toast.LENGTH_SHORT).show();
                }



        }
    });


        radioGroup.clearCheck();



}


    private void initViews() {

/*
        mCameraLayout =findViewById(R.id.linearLayout);
*/
        btnScanOrderNo = findViewById(R.id.btnScanOrderNo);
        etOrderNum = findViewById(R.id.etOrderNo);
        etSignedBy = findViewById(R.id.orderNo8);
        etQtyShortage = findViewById(R.id.orderNo7);
        etPartNum = findViewById(R.id.partNumber);
        btnSubmit = findViewById(R.id.button2);
        imageView = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progressBar);
        btnBack = findViewById(R.id.btnBack);
        radioGroup = findViewById(R.id.radioGrpLoc);



       /* recieveDate.setFocusable(false);
        recieveDate.setClickable(true);
        movingDate.setFocusable(false);
        movingDate.setClickable(true);
*/

        pref = new SharedPref(this);

        etSignedBy.setText("Signed By: "+pref.getUserName());
        etOrderNum.requestFocus();
        //pref.setLastUpdatedTime(System.currentTimeMillis());

        mDatabase = FirebaseDatabase.getInstance().getReference().child(BuildConfig.BASE_TABLE).child(BuildConfig.MATERIAL_ISSUE_TABLE);
        mStorageRef = FirebaseStorage.getInstance().getReference().child(BuildConfig.BASE_TABLE).child(BuildConfig.MATERIAL_ISSUE_TABLE);

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
        AlertDialog.Builder builder = new AlertDialog.Builder(MaterialIssueActivity.this);
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

                    /*Intent i = new Intent(JobCompletionActivity.this,CameraActivity.class);
                    startActivity(i);*/

                    //Material Camera Library

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

   /* public void uploadImage(String filePath, String fileName, final MaterialIssueDetails productTable){

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
                                    Log.d("DOWNLOAD PATH", "onSuccess: uri= "+ uri.toString());
                                    String outputurl = uri.toString();
                                    productTable.PO_image=(outputurl);
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

    }*/

    public void updateDb(MaterialIssueDetails productTable) {
        mDatabase.push().setValue(productTable);

        fileName = "";
        filePathNew = "";
        locFrom = "";
        locTo = "";
        movDate = "";
        recDate = "";
        orderNum = "";
        signedBy = "";

//        imageView.setImageDrawable(null);
        Toast.makeText(activity, "Successfully Added", Toast.LENGTH_SHORT).show();
        //pref.setLastUpdatedTime(System.currentTimeMillis());
        finish();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        // This is important, otherwise the result will not be passed to the fragment
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_RQ) {


                //Material Camera libarry

//                    Toast.makeText(this, "Saved to: " + data.getDataString(), Toast.LENGTH_LONG).show();

//                    File f = new File(data.getDataString());
//                    Bitmap bitmap;
//                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
//                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
//                            bitmapOptions);
//                    imageView.setImageBitmap(bitmap);
//                    imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 120, 120, false));

                Picasso.get().load(data.getDataString()).resize(120, 120).centerCrop().placeholder(R.drawable.ic_camera).into(imageView);

//                fileName = "pic.jp";
                filePathNew = data.getDataString();
                fileName = filePathNew.substring(filePathNew.lastIndexOf("/") + 1);
                //Custom camera and default camera

                   /* File f = new File(Environment.getExternalStorageDirectory().toString());
                    for (File temp : f.listFiles()) {
                        if (temp.getName().equals("temp.jpg")) {
                            f = temp;
                            break;
                        }
                    }
                    try {
                        Bitmap bitmap;
                        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                        bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                                bitmapOptions);
                        imageView.setImageBitmap(bitmap);
                        imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 120, 120, false));


                        saveFile(bitmap);

                        *//*String path = Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES)+ File.separator
                                + "Phoenix" + File.separator + "default";
                        File newFile=new File(path);
                        if(!newFile.exists()){
                            newFile.mkdir();
                        }
                                *//**//*android.os.Environment
                                .getExternalStorageDirectory()
                                + File.separator
                                + "Phoenix" + File.separator + "default";*//**//*
                        //f.delete();
                        OutputStream outFile = null;
                        fileName = String.valueOf(System.currentTimeMillis()) + ".jpg";
                        File file = new File(path, fileName);
                        filePathNew = file.getAbsolutePath();


                            file.mkdir();

                        System.out.println("Filepath:: "+filePathNew);
                        try {
                             outFile = new FileOutputStream(f);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                            outFile.flush();
                            outFile.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }*//*



                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/
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
                etOrderNum.setText(message);

                etPartNum.requestFocus();
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
