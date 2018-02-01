package com.app.owly.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

/**
 * Created by Christiane on 1/30/18.
 */

public class User {

    public String username;
    public String email;
    public Boolean isRegistered;
    public Boolean isSignedIn;
    public Boolean onGoingSession;
    public Boolean isNotified;
    public Boolean insideSession;
    public String currentSession;
    public String currentAlert;
    public String currentSecondUser;
    public String currentCircle;
    public Boolean demo;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
        this.isRegistered = false;
        this.isSignedIn = false;
        this.onGoingSession = false;
        this.isNotified = false;
        this.insideSession = false;
        this.currentSession = "";
        this.currentAlert = "";
        this.currentSecondUser = "";
        this.currentCircle = "";
        this.demo = false;

    }

    static void writeNewUser(String userId, String username, String email) {
        Log.d("writeNewUser", "writeNewUser was called");
        User user = new User(username, email);
        DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference("users");
        userDatabase.child(userId).setValue(user);
        Log.d("writeNewUser", "userDatabase: " + userDatabase);
        Log.d("writeNewUser", "userDatabase + uid: " + userDatabase.child(userId));
    }

    public static void isAuthenticated(Context context){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            Toast.makeText(context, "Something went wrong. Please, sign-in again.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);

        }else{
            Log.d("User class", "User is authenticated");
        }
    }

}
