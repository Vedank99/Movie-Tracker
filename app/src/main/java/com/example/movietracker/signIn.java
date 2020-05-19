package com.example.movietracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class signIn extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    EditText emailIn,passIn;
    Button signInBut;
    Intent intent;
    ProgressBar signInProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        firebaseAuth = FirebaseAuth.getInstance();

        emailIn = findViewById(R.id.signInEmail);
        passIn = findViewById(R.id.signInPass);
        signInBut = findViewById(R.id.signInButton);
        signInProgress = findViewById(R.id.signInProgress);

        signInBut.setOnClickListener(v -> {
            String email = emailIn.getText().toString();
            String pass = passIn.getText().toString();

            if (email.isEmpty()) {
                emailIn.setError("Please enter email id..");
                emailIn.requestFocus();
            } else if (pass.isEmpty()) {
                passIn.setError("Please enter password..");
                passIn.requestFocus();
            } else {
                signInProgress.setVisibility(View.VISIBLE);
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        signInProgress.setVisibility(View.GONE);
                        Log.d("Authentication", "User signed in..");
                        intent = new Intent(signIn.this, HomeScreen.class);
                        intent.putExtra("state", "signingIn");
                        startActivity(intent);
                        finish();
                    } else {
                        signInProgress.setVisibility(View.GONE);
                        Toast.makeText(signIn.this, "Wrong email or password.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
