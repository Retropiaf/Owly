package com.app.owly.login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.owly.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.apache.commons.lang3.StringUtils;

public class RegisterActivity extends AppCompatActivity {


    private static final String TAG = "RegisterActivity";

    private DatabaseReference databaseMainUsers;
    private EditText email, password, username, confirmEmail, confirmPassword;
    private String inputUsername, inputEmail, inputConfirmEmail, inputPassword, inputConfirmPassword;
    private Button register;
    private TextView login;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // View elements present from the start
        username = (EditText) findViewById(R.id.register_username);
        email = (EditText) findViewById(R.id.register_email);
        confirmEmail = (EditText) findViewById(R.id.register_confirm_email);
        password = (EditText) findViewById(R.id.register_password);
        confirmPassword = (EditText) findViewById(R.id.register_confirm_password);
        register = (Button) findViewById(R.id.register_confirm_btn);
        login = findViewById(R.id.register_login);
        progressBar = (ProgressBar) findViewById(R.id.register_progress);

        // Data entered by user
        inputUsername = username.getText().toString();
        inputEmail = email.getText().toString();
        inputConfirmEmail = confirmEmail.getText().toString();
        inputPassword = password.getText().toString();
        inputConfirmPassword = confirmPassword.getText().toString();

        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus){
                    String input = username.getText().toString().toLowerCase();
                    username.setText(StringUtils.capitalize(input));
                }
            }
        });

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus){
                    String input = email.getText().toString().toLowerCase();
                    email.setText(input);
                }
            }
        });

        confirmEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus){
                    String input = confirmEmail.getText().toString().toLowerCase();
                    confirmEmail.setText(input);
                }
            }
        });


        confirmEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String confirmInput = String.valueOf(charSequence);
                String input = email.getText().toString().toLowerCase();
                if(confirmInput.toLowerCase().equals(input.toLowerCase())){
                    confirmEmail.setVisibility(View.GONE);
                    password.requestFocus();
                }
                if(!confirmInput.toLowerCase().equals(input.toLowerCase())){
                    confirmEmail.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String confirmInput = String.valueOf(charSequence);
                String input = password.getText().toString();
                if(confirmInput.equals(input)){
                    confirmPassword.setVisibility(View.GONE);
                    password.requestFocus();
                }
                if(!confirmInput.equals(input)){
                    confirmPassword.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String input = String.valueOf(charSequence);
                String confirmInput = confirmEmail.getText().toString();
                if (isEmpty(confirmInput) && isAnEmail(input)){
                    confirmEmail.setVisibility(View.VISIBLE);
                }else if(isEmpty(input)){
                    confirmEmail.setVisibility(View.GONE);
                    confirmEmail.setText("");
                }else if(confirmInput.toLowerCase().equals(input.toLowerCase())){
                    confirmEmail.setVisibility(View.GONE);

                    RegisterActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                    confirmPassword.requestFocus();

                }else if (!isEmpty(confirmInput)){
                    confirmEmail.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String input = String.valueOf(charSequence);
                String confirmInput = confirmPassword.getText().toString();
                if (isEmpty(confirmInput) && !isEmpty(input)){
                    confirmPassword.setVisibility(View.VISIBLE);
                }else if(isEmpty(input)){
                    confirmPassword.setVisibility(View.GONE);
                    confirmPassword.setText("");
                }else if(!isEmpty(confirmInput) && confirmInput.equals(input)){
                    confirmPassword.setVisibility(View.GONE);
                }else if (!isEmpty(confirmInput) && !input.equals(confirmInput)){
                    confirmPassword.setVisibility(View.VISIBLE);
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
                hideSoftKeyboard();
                validate();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

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

    private void validate(){
        Log.d(TAG, "Inside validate");
        inputUsername = username.getText().toString();
        inputEmail = email.getText().toString();
        inputConfirmEmail = confirmEmail.getText().toString();
        inputPassword = password.getText().toString();
        inputConfirmPassword = confirmPassword.getText().toString();
        Log.d(TAG, "inputUsername: " + inputUsername);
        Log.d(TAG, "inputEmail: " + inputEmail);
        Log.d(TAG, "inputConfirmEmail: " + inputConfirmEmail);
        Log.d(TAG, "inputPassword: " + inputPassword);
        Log.d(TAG, "inputConfirmPassword: " + confirmPassword);



        if (isEmpty(inputUsername)) {
            Log.d(TAG, "Inside validate 2");
            Toast.makeText(RegisterActivity.this, "You must enter a username.", Toast.LENGTH_SHORT).show();
        } else if (isEmpty(inputEmail)) {
            Log.d(TAG, "Inside validate 3");
            Toast.makeText(RegisterActivity.this, "You must enter an email.", Toast.LENGTH_SHORT).show();
        } else if (isEmpty(inputConfirmEmail)) {
            Log.d(TAG, "Inside validate 4");
            Toast.makeText(RegisterActivity.this, "You must confirm your email", Toast.LENGTH_SHORT).show();
        } else if (isEmpty(inputPassword)) {
            Log.d(TAG, "Inside validate 5");
            Toast.makeText(RegisterActivity.this, "You must enter a password.", Toast.LENGTH_SHORT).show();
        } else if (isEmpty(inputConfirmPassword)) {
            Log.d(TAG, "Inside validate 6");
            Toast.makeText(RegisterActivity.this, "You must confirm your password.", Toast.LENGTH_SHORT).show();
        } else if (!inputConfirmEmail.equals(inputEmail)) {
            Log.d(TAG, "Inside validate 7");
            Toast.makeText(RegisterActivity.this, "Your emails do not match.", Toast.LENGTH_SHORT).show();
        } else if (!inputConfirmPassword.equals(inputPassword)) {
            Log.d(TAG, "Inside validate 8");
            Toast.makeText(RegisterActivity.this, "Your passwords do not match.", Toast.LENGTH_SHORT).show();
        }else{
            Log.d(TAG, "Validation passed");
            registerNewEmail(inputEmail, inputPassword);
        }


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
        Log.d(TAG, "Inside redirectLoginScreen");
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void addUserToDatabase() {
        Log.d(TAG, "Inside addUserToDatabase");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null){
            Log.d(TAG, "User is logged in");
            String uid = user.getUid();
            String email = user.getEmail();
            String userName = inputUsername;

            User.writeNewUser(uid, userName, email);

        }else{
            Log.d(TAG, "addUserToDatabase: failed to add main user to the database ");
        }


    }



    private void sendVerificationEmail(){
        Log.d(TAG, "Inside sendVerificationEmail");
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

    private boolean isAnEmail(String target){
        return Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
