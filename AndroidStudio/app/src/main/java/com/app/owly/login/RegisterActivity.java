package com.app.owly.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.app.owly.R;
import com.google.firebase.database.DatabaseReference;

public class RegisterActivity extends AppCompatActivity {


    private static final String TAG = "RegisterActivity";

    DatabaseReference databaseMainUsers;

    private EditText mEmail, mPassword, mConfirmPassword, mUsername;
    private Button mRegister;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }
}
