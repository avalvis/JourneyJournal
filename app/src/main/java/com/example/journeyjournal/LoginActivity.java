package com.example.journeyjournal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    // Declare UI elements
    EditText emailEditText, passwordEditText;
    Button loginBtn;
    ProgressBar progressBar;
    TextView registerBtnTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI elements
        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        loginBtn = findViewById(R.id.login_btn);
        progressBar = findViewById(R.id.progress_bar);
        registerBtnTextView = findViewById(R.id.register_text_view_btn);

        // Set click listener for login button
        loginBtn.setOnClickListener(v -> { login(); });

        // Set click listener for register button
        registerBtnTextView.setOnClickListener((v)->startActivity(new Intent(LoginActivity.this,RegisterActivity.class)) );
    }

    // Method to handle login
    void login(){
        String email  = emailEditText.getText().toString();
        String password  = passwordEditText.getText().toString();

        // Validate user input
        boolean isValidated = validateData(email,password);
        if(!isValidated){
            return;
        }

        // Attempt to login with Firebase
        loginInFirebase(email,password);
    }

    // Method to handle Firebase login
    void loginInFirebase(String email, String password){
        // Show progress bar
        changeInProgress(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        // Attempt to sign in with email and password
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
            // Hide progress bar
            changeInProgress(false);
            if(task.isSuccessful()){
                // If login is successful, navigate to main activity
                Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }else{
                // If login fails, show error message
                Toast.makeText(LoginActivity.this, Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to show or hide progress bar and login button
    void changeInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            loginBtn.setVisibility(View.VISIBLE);
        }
    }

    // Method to validate user input
    boolean validateData(String email, String password){
        // Check if email is empty
        if(email.isEmpty()){
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            return false;
        }
        // Check if email format is valid
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Email format is invalid");
            return false;
        }
        // Check if password is empty
        if(password.isEmpty()){
            passwordEditText.setError("Password is required");
            passwordEditText.requestFocus();
            return false;
        }
        // Check if password length is at least 6 characters
        if(password.length() < 6){
            passwordEditText.setError("Password must be at least 6 characters");
            passwordEditText.requestFocus();
            return false;
        }

        return true;
    }
}