package com.app.owly.login;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;

import com.app.owly.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.app.owly.login.User;

public class RegisterActivity extends AppCompatActivity {


    private static final String TAG = "RegisterActivity";

    private DatabaseReference databaseMainUsers;
    private EditText email, password, username, confirmEmailEditText, confirmPasswordEditText;
    private String inputUsername, inputEmail, inputConfirmEmail, inputPassword, inputConfirmPassword;
    private TextView title;
    private Button register;
    private RelativeLayout registerLayout;
    private LayoutInflater inflater;
    private View confirmEmail, confirmPassword;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // View elements present from the start
        username = (EditText) findViewById(R.id.register_username);
        email = (EditText) findViewById(R.id.register_email);
        password = (EditText) findViewById(R.id.register_password);
        register = (Button) findViewById(R.id.register_confirm_btn);
        progressBar = (ProgressBar) findViewById(R.id.register_progress);


        // View element that appears as/after the user filling info
        registerLayout = findViewById(R.id.register_activity);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        confirmEmail = inflater.inflate(R.layout.confirm_email, null);
        confirmPassword = inflater.inflate(R.layout.confirm_password, null);

        // Get EditText elements after they appear
        confirmEmailEditText = findViewById(R.id.register_confirm_email);
        confirmPasswordEditText = findViewById(R.id.register_confirm_password);


        // Data entered by user
        inputUsername = username.getText().toString();
        inputEmail = email.getText().toString();
        inputPassword = password.getText().toString();
        inputConfirmPassword = confirmPasswordEditText.getText().toString();
        inputConfirmEmail = confirmEmailEditText.getText().toString();

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
               if(!hasFocus){
                   if(isEmpty(inputEmail)){
                       try {
                           registerLayout.removeView((View) confirmEmail.getParent());
                       } catch (Exception e) {
                           e.printStackTrace();
                       }
                   }else{
                       // add the rule that places your button below your EditText object
                       confirmEmail.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.BELOW, email.getId()));
                       registerLayout.addView(confirmEmail);
                   }
               }
            }
        });

        confirmEmailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(inputConfirmEmail == inputEmail){
                    try {
                        registerLayout.removeView((View) confirmEmail.getParent());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus){
                    if(isEmpty(inputPassword)){
                        try {
                            registerLayout.removeView((View) confirmPassword.getParent());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else{
                        // add the rule that places your button below your EditText object
                        confirmPassword.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.BELOW, password.getId()));
                        registerLayout.addView(confirmPassword);
                    }
                }
            }
        });

        confirmPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(inputConfirmPassword == inputPassword){
                    try {
                        registerLayout.removeView((View) confirmPassword.getParent());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        databaseMainUsers = FirebaseDatabase.getInstance().getReference("MainUsers");
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    registerNewEmail(inputEmail, inputPassword);
                }
            }
        });

        hideSoftKeyboard();
    }

    private void registerNewEmail(final String email, String password){

        showDialog();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {

                    addUserToDatabase();
                    sendVerificationEmail();
                    FirebaseAuth.getInstance().signOut();

                    //redirect the user to the login screen
                    redirectLoginScreen();
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(RegisterActivity.this, "Sorry, we were unable to register you.", Toast.LENGTH_SHORT).show();
                }
                hideDialog();
            }

        });


    }


    private boolean isEmpty(String string){
        return string.equals("");
    }

    private boolean validate(){
        if (isEmpty(inputUsername)) {
            Toast.makeText(RegisterActivity.this, "You must enter a username.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (isEmpty(inputEmail)) {
            Toast.makeText(RegisterActivity.this, "You must enter an email.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (isEmpty(inputConfirmEmail)) {
            Toast.makeText(RegisterActivity.this, "You must confirm your email", Toast.LENGTH_SHORT).show();
            return false;
        } else if (isEmpty(inputPassword)) {
            Toast.makeText(RegisterActivity.this, "You must enter a password.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (isEmpty(inputConfirmPassword)) {
            Toast.makeText(RegisterActivity.this, "You must confirm your password.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!inputConfirmEmail.equals(inputEmail)) {
            Toast.makeText(RegisterActivity.this, "Your emails do not match.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!inputConfirmPassword.equals(inputPassword)) {
            Toast.makeText(RegisterActivity.this, "Your passwords do not match.", Toast.LENGTH_SHORT).show();
        }
        return true;

    }

    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void showDialog(){
        progressBar.setVisibility(View.VISIBLE);

    }

    private void hideDialog(){
        if(progressBar.getVisibility() == View.VISIBLE){
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void redirectLoginScreen(){
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void addUserToDatabase() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null){
            String uid = user.getUid();
            String email = user.getEmail();
            String userName = inputUsername;

            User.writeNewUser(uid, userName, email);

        }else{
            Log.d(TAG, "addUserToDatabase: failed to add main user to the database ");
        }


    }



    private void sendVerificationEmail(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "Check your inbox for a registration email.", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(RegisterActivity.this, "Oops, we couldn't sent the verification email.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }
}
