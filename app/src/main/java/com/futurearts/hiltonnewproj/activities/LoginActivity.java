package com.futurearts.hiltonnewproj.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.UserHandle;
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
import com.futurearts.hiltonnewproj.modelclasses.LoginDetails;
import com.futurearts.hiltonnewproj.models.login.Data;
import com.futurearts.hiltonnewproj.models.login.Loginapi;
import com.futurearts.hiltonnewproj.utils.SharedPref;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;


import java.io.IOError;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
//        ip = "192.168.2.80";
        ip = "12.0.2269.0";
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
                    //checkDb(userNamePassword);
                    LoginPHP(userName,password);

//                    SQLServerConnect();
//                    new UploadImage(userName,password).execute();
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




    public class UploadImage extends AsyncTask<String,String,String>
    {
        String userName, passWord;
        public UploadImage(String userName, String password) {

            this.userName = userName;
            this.passWord = password;

        }

        @Override
        protected void onPostExecute(String r)
        {
            // After successful insertion of image
            progressBar.setVisibility(View.GONE);
            Toast.makeText(LoginActivity.this , r , Toast.LENGTH_LONG).show();
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
//                String commands = "select * from dbo.Users";
                String commands = "insert into dbo.Users (Username,Password) values ('"+ userName+"','"+passWord +"');";
                System.out.println("Command:: "+commands);
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

            return msg;
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
//            ConnectionURL = "jdbc:jtds:sqlserver://" + server+ database + ";user=" + user + ";password=" + password + ";"+"instance=VS004\\SIGNONGLASS;";
//            ConnectionURL = "jdbc:jtds:sqlserver://"+ip+":1433;databaseName="+database+";user=" + user + ";password=" + password + ";"+"instance=VS004\\SIGNONGLASS";
            ConnectionURL = "jdbc:jtds:sqlserver://192.168.1.52\\JJAppDev;user=HILTONMFG\\JacobJ;password=$uperTwist73;instance=VS004\\SIGNONGLASS;";
            connection = DriverManager.getConnection(ConnectionURL);
        }
        catch (final SQLException se)
        {
            Log.e("error no 1", se.getMessage());
            
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(LoginActivity.this, se.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
        catch (final ClassNotFoundException e)
        {
            Log.e("error no 2", e.getMessage());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
        catch (final Exception e)
        {
            Log.e("error no 3", e.getMessage());

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        return connection;
    }

    private void LoginPHP(final String userName, final String password) {

        String URL;
        URL = Constants.BASE_URL + "login.php";
        //System.out.println("CHECK---> URL " + URL);
        StringRequest jsonObjectRequestLogin = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String jsonObject) {
                //System.out.println("CHECK---> Response " + jsonObject);
                progressBar.setVisibility(View.GONE);



                AppClass.getInstance().cancelPendingRequests("login");
                Gson gson = new Gson();
                Loginapi loginapi =gson.fromJson(jsonObject, Loginapi.class);

                int errorCode = loginapi.getErrorCode();
                String message = loginapi.getMessage();
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();

                if (errorCode == 0) {

                    Data  data = loginapi.getData();

                    String userID = data.getId();

                    pref.setUserId(userID);
                    pref.setUserName(userName);

                    //pref.setLastUpdatedTime(System.currentTimeMillis());
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();

                }else  if (errorCode == 1) {



                }



            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressBar.setVisibility(View.GONE);
                AppClass.getInstance().cancelPendingRequests("login");
                VolleyLog.d("Object Error : ", volleyError.getMessage());

                Toast.makeText(LoginActivity.this, volleyError.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", userName);
                params.put("password", password);
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


}
