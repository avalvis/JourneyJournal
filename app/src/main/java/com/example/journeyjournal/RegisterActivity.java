package com.example.journeyjournal;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    // Declare UI elements
    EditText usernameEditText, emailEditText, passwordEditText;
    Button registerBtn;
    ProgressBar progressBar;
    TextView loginBtnTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize UI elements
        usernameEditText = findViewById(R.id.username_edit_text);
        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        registerBtn = findViewById(R.id.register_btn);
        progressBar = findViewById(R.id.progress_bar);
        loginBtnTextView = findViewById(R.id.login_text_view_btn);

        // Set click listener for register button
        registerBtn.setOnClickListener(v -> { register(); });

        // Set click listener for login button
        loginBtnTextView.setOnClickListener(v -> { finish(); });
    }

    // Method to handle registration
    void register(){
        String username  = usernameEditText.getText().toString();
        String email  = emailEditText.getText().toString();
        String password  = passwordEditText.getText().toString();

        // Validate user input
        boolean isValidated = validateData(username,email,password);
        if(!isValidated){
            return;
        }

        // Attempt to register with Firebase
        registerInFirebase(username,email,password);
    }

    // Method to handle Firebase registration
    void registerInFirebase(String username, String email, String password){
        // Show progress bar
        changeInProgress(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Check if username is already in use
        db.collection("users").whereEqualTo("username", username).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().isEmpty()) {
                    // Username is not in use, proceed with registration
                    firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task1 -> {
                        changeInProgress(false);
                        if(task1.isSuccessful()){
                            // Account creation is successful, store user data in Firestore
                            Toast.makeText(RegisterActivity.this, "Successfully created account", Toast.LENGTH_SHORT).show();
                            String uid = firebaseAuth.getCurrentUser().getUid();
                            Map<String, Object> user = new HashMap<>();
                            user.put("username", username);
                            user.put("uid", uid);
                            user.put("email", email); // Storing email
                            db.collection("users").document(uid).set(user).addOnCompleteListener(task2 -> {
                                if(task2.isSuccessful()){
                                    // Data stored successfully, sign out and finish activity
                                    firebaseAuth.signOut();
                                    finish();
                                } else {
                                    // Failure in storing data
                                    Toast.makeText(RegisterActivity.this, Objects.requireNonNull(task2.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            // Failure in account creation
                            Toast.makeText(RegisterActivity.this, Objects.requireNonNull(task1.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Username is in use, show an error
                    changeInProgress(false);
                    Toast.makeText(RegisterActivity.this, "Username is already in use", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Failure in checking username
                changeInProgress(false);
                Toast.makeText(RegisterActivity.this, Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to show or hide progress bar and register button
    void changeInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            registerBtn.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            registerBtn.setVisibility(View.VISIBLE);
        }
    }

    // Method to validate user input
    boolean validateData(String username, String email, String password){
        // Check if username is empty
        if(username.isEmpty()){
            usernameEditText.setError("Username is required");
            usernameEditText.requestFocus();
            return false;
        }
        // Check if email is empty
        if(email.isEmpty()){
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            return false;
        }
        // Check if email format is valid
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
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
        // Check if password contains at least one digit
        if(!password.matches(".*\\d.*")){
            passwordEditText.setError("Password must contain at least one digit");
            passwordEditText.requestFocus();
            return false;
        }

        return true;
    }
}