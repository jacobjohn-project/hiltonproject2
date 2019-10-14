package com.futurearts.hiltonnewproj.activities;

import android.content.Intent;
import android.os.Bundle;
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
import com.futurearts.hiltonnewproj.utils.SharedPref;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText editText, editText2;
    Button button;
    ProgressBar progressBar;
    SharedPref pref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


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
                    checkDb(userNamePassword);
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

}
