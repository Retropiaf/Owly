package com.app.owly.login;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Christiane on 1/30/18.
 */

public class User {

    public String username;
    public String email;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    static void writeNewUser(String userId, String username, String email) {
        Log.d("writeNewUser", "writeNewUser was called");
        User user = new User(username, email);
        DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference("users");
        userDatabase.child(userId).setValue(user);
        Log.d("writeNewUser", "userDatabase: " + userDatabase);
        Log.d("writeNewUser", "userDatabase + uid: " + userDatabase.child(userId));
    }
}
