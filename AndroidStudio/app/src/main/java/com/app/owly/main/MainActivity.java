package com.app.owly.main;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.app.owly.R;
import com.app.owly.login.LoginActivity;
import com.app.owly.login.User;
import com.app.owly.sleepCircle.NewSleepCircleActivity;
import com.app.owly.sleepCircle.SleepCirclesActivity;
import com.app.owly.sleepSession.NewSleepSessionActivity;
import com.app.owly.sleepSession.SleepSessionsActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    Button sleepSessionsBtn, sleepCirclesBtn, newSleepSessionBtn, newSleepCircleBtn, accountBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        User.isAuthenticated(this);

        sleepSessionsBtn = findViewById(R.id.sleep_sessions_btn);
        sleepCirclesBtn = findViewById(R.id.sleep_circles_btn);
        newSleepSessionBtn = findViewById(R.id.new_sleep_btn);
        newSleepCircleBtn = findViewById(R.id.add_sleep_circle_btn);
        accountBtn = findViewById(R.id.user_account_btn);

        sleepSessionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openSleepSessions = new Intent (MainActivity.this, SleepSessionsActivity.class);
                openSleepSessions.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(openSleepSessions);
                finish();
            }
        });
        newSleepSessionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openNewSleepSession = new Intent (MainActivity.this, NewSleepSessionActivity.class);
                openNewSleepSession.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(openNewSleepSession);
                finish();
            }
        });
        sleepCirclesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openSleepCircles = new Intent (MainActivity.this, SleepCirclesActivity.class);
                openSleepCircles.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(openSleepCircles);
                finish();
            }
        });
        newSleepCircleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openNewSleepCircle = new Intent (MainActivity.this, NewSleepCircleActivity.class);
                openNewSleepCircle.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(openNewSleepCircle);
                finish();
            }
        });

        accountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Successfully signed out.", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });




    }
}
