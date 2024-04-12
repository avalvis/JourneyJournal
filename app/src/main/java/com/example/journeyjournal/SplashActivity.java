package com.example.journeyjournal;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Find the TextView for the app name
        TextView appName = findViewById(R.id.appName);

        // Load the zoom in animation
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.zoom_in);

        // Start the animation on the app name TextView
        appName.startAnimation(anim);

        // Create a new Handler to delay the execution of the Runnable
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Get the current user from Firebase
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                // If there is no current user, start the LoginActivity
                if(currentUser==null){
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                }else{
                    // If there is a current user, start the MainActivity
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                }

                // Finish the SplashActivity so it is removed from the back stack
                finish();
            }
        }, 3000); // 3000 ms delay for the splash screen
    }
}