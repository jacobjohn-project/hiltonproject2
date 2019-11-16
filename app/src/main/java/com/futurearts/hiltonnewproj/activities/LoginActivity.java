package com.futurearts.hiltonnewproj.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.AndroidRuntimeException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.futurearts.hiltonnewproj.BuildConfig;
import com.futurearts.hiltonnewproj.R;
import com.futurearts.hiltonnewproj.modelclasses.LoginDetails;
import com.futurearts.hiltonnewproj.utils.SharedPref;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.io.IOError;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class LoginActivity extends AppCompatActivity {

    EditText editText, editText2;
    Button button;
    ProgressBar progressBar;
    SharedPref pref;


    Connection con;
    String un,ip,db,password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // Declaring connectivity credentials
        ip = "192.168.0.200/manorama_new/";
        db = "JJAppDev";
        un = "HILTONMFG\\JacobJ";
        password = "$uperTwist73";
        // End Declaring connectivity credentials

        initViews();



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userName = editText.getText().toString();
                String password = editText2.getText().toString();
                if (userName.equals("")) {
                    editText2.setError(null);
                    editText.setError("Enter user Id");
                } else if (password.equals("")) {
                    editText.setError(null);
                    editText2.setError("Enter password");
                } else {
                    editText.setError(null);
                    editText2.setError(null);
                    String userNamePassword = userName + "_" + password;

                    progressBar.setVisibility(View.VISIBLE);
//                    checkDb(userNamePassword);

//                    SQLServerConnect();
                    new UploadImage().execute();
                }

            }
        });
    }

    private void initViews() {
        editText = findViewById(R.id.editText);
        editText2 = findViewById(R.id.editText2);
        button = findViewById(R.id.button);
        progressBar = findViewById(R.id.progressBar);
        pref = new SharedPref(this);
    }



    private void checkDb(String userNamePassowrd) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child(BuildConfig.BASE_TABLE).child(BuildConfig.AUTH_TABLE);
        mDatabase.keepSynced(true);
        mDatabase.orderByChild("userName_password").equalTo(userNamePassowrd).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot post : dataSnapshot.getChildren()) {
                        pref.setUserId(post.getKey());
                        LoginDetails loginDetails=post.getValue(LoginDetails.class);
                        pref.setUserName(loginDetails.getUserName());
                        //pref.setLastUpdatedTime(System.currentTimeMillis());
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid credentials.", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled (@NonNull DatabaseError databaseError){

            }
        });
    }


    private void SQLServerConnect(){

        Log.i("Android"," MySQL Connect Example.");
        Connection conn = null;
        try {
            String driver = "net.sourceforge.jtds.jdbc.Driver";
            Class.forName(driver).newInstance();
//test = com.microsoft.sqlserver.jdbc.SQLServerDriver.class;
            String connString = "jdbc:jtds:sqlserver://192.168.2.80/JJAppDev;encrypt=false;user=HILTONMFG\\JacobJ;password=$uperTwist73;instance=VS004\\SIGNONGLASS;";
            String username = "HILTONMFG\\JacobJ";
            String password = "$uperTwist73";
            conn = DriverManager.getConnection(connString);
            Log.w("Connection","open");
            Statement stmt = conn.createStatement();
            ResultSet reset = stmt.executeQuery("select * from dbo.Account");


            while(reset.next()){
                Log.w("Data:",reset.getString(3));
                Log.w("Data",reset.getString(2));
            }
            conn.close();

        } catch (Exception e)
        {
            Log.w("Error connection","" + e.getMessage());
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    public class UploadImage extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPostExecute(String r)
        {
            // After successful insertion of image
            progressBar.setVisibility(View.GONE);
            Toast.makeText(LoginActivity.this , "Image Inserted Succesfully" , Toast.LENGTH_LONG).show();
            // End After successful insertion of image
        }
        @Override
        protected String doInBackground(String... params)
        {
            // Inserting in the database
            String msg = "unknown";
            try
            {
                con = connectionclass(un,password,db,ip);
                String commands = "select * from dbo.Users";
                PreparedStatement preStmt = con.prepareStatement(commands);
                preStmt.executeUpdate();
                msg = "Inserted Successfully";
            }
            catch (SQLException ex)
            {
                msg = ex.getMessage().toString();
                Log.d("Error no 1:", msg);

            }
            catch (IOError ex)
            {
                msg = ex.getMessage().toString();
                Log.d("Error no 2:", msg);

            }
            catch (AndroidRuntimeException ex)
            {
                msg = ex.getMessage().toString();
                Log.d("Error no 3:", msg);

            }
            catch (NullPointerException ex)
            {
                msg = ex.getMessage().toString();
                Log.d("Error no 4:", msg);

            }
            catch (Exception ex)
            {
                msg = ex.getMessage().toString();
                Log.d("Error no 5:", msg);

            }
            System.out.println(msg);

            return "";
            //End Inserting in the database
        }
    }



    @SuppressLint("NewApi")
    public Connection connectionclass(String user, String password, String database, String server)
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;
        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://" + server+ database + ";user=" + user + ";password=" + password + ";"+"instance=VS004\\SIGNONGLASS;";
            connection = DriverManager.getConnection(ConnectionURL);
        }
        catch (SQLException se)
        {
            Log.e("error no 1", se.getMessage());

        }
        catch (ClassNotFoundException e)
        {
            Log.e("error no 2", e.getMessage());

        }
        catch (Exception e)
        {
            Log.e("error no 3", e.getMessage());

        }
        return connection;
    }


}
