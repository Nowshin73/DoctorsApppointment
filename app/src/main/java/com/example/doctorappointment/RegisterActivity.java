package com.example.doctorappointment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.*;

public class RegisterActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPassword, editTextUserType;
    Button buttonRegister;

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextUserType = findViewById(R.id.editTextUserType);
        buttonRegister = findViewById(R.id.buttonRegister);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        buttonRegister.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        String userType = editTextUserType.getText().toString().toLowerCase();

        if (email.isEmpty() || password.isEmpty() || userType.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String uid = mAuth.getCurrentUser().getUid();
                        // Save user type to Firestore
                        DocumentReference userRef = db.collection("users").document(uid);
                        userRef.set(new User(email, userType))
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(this, "Registered successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Failed to save user type", Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    public class User {
        public String email, userType;

        public User() {}

        public User(String email, String userType) {
            this.email = email;
            this.userType = userType;
        }
    }
}
