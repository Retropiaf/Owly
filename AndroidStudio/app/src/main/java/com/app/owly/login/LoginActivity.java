package com.app.owly.login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.owly.R;
import com.app.owly.main.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    DatabaseReference database;
    FirebaseUser user;
    private FirebaseAuth.AuthStateListener authListener;
    private EditText email, password;
    private ProgressBar progressBar;
    private Button signIn;
    private TextView resetPassword, resendEmailVerification, register;
    private static final String TAG = "LoginActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user = FirebaseAuth.getInstance().getCurrentUser();


        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        progressBar = findViewById(R.id.login_progress);
        signIn = findViewById(R.id.login_confirm_btn);
        resetPassword = findViewById(R.id.login_forgot_password);
        resendEmailVerification = findViewById(R.id.login_resend_email);
        register = findViewById(R.id.login_register);

        setupFirebaseAuth();

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //check if the fields are filled out
                if(!isEmpty(email.getText().toString())
                        && !isEmpty(password.getText().toString())){
                    Log.d(TAG, "onClick: attempting to authenticate.");

                    showDialog();



                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            hideDialog();


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(LoginActivity.this, "You didn't fill in all the fields.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private boolean isEmpty(String string){
        return string.equals("");
    }


    private void showDialog(){
        progressBar.setVisibility(View.VISIBLE);

    }

    private void hideDialog(){
        if(progressBar.getVisibility() == View.VISIBLE){
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: started.");

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(user != null){

                    final String uid = user.getUid();

                    if(user.isEmailVerified()){
                        Log.d(TAG, "inside setupFirebaseAuth");


                        Log.d(TAG, "onAuthStateChanged: signed_in: " + user.getUid());
                        Toast.makeText(LoginActivity.this, "Authenticated with: " + user.getEmail(), Toast.LENGTH_SHORT).show();

                        database = FirebaseDatabase.getInstance().getReference().child("users");

                        Map<String, Object> childUpdates = new HashMap<>();
                        Log.d(TAG, "Got here");
                        childUpdates.put(user.getUid() + "/isRegistered/", true);
                        childUpdates.put(user.getUid() + "/isSignedIn/", true);
                        database.updateChildren(childUpdates);


                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        Log.d(TAG, "Taking you to the Main user welcome screen, hopefully!");
                        finish();

                    }else{
                        Toast.makeText(LoginActivity.this, "Check Your Email Inbox for a Verification Link", Toast.LENGTH_SHORT).show();

                        FirebaseAuth.getInstance().signOut();
                    }

                }else{
                    Log.d(TAG, "onAuthStateChanged: signed_out");
                }
            }
        };

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        resendEmailVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user.sendEmailVerification()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "Email sent! Check your inbox.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

        hideSoftKeyboard();
    }





    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(authListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authListener != null){
            FirebaseAuth.getInstance().removeAuthStateListener(authListener);
        }
    }




}
