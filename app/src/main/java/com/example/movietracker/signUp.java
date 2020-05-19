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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class signUp extends AppCompatActivity {

    Intent intent;

    EditText emailUp,passUp,userName;
    Button signUp;
    TextView toSignIn;
    ProgressBar signUpProgress;
    private FirebaseAuth firebaseAuth;
    //boolean signUpButtonPress = false;
    //boolean signInButtonPress = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);


        emailUp = findViewById(R.id.signUpEmail);
        passUp = findViewById(R.id.signUpPass);
        signUp = findViewById(R.id.signUpButton);
        userName = findViewById(R.id.signUpUserName);
        toSignIn = findViewById(R.id.toSignIn);
        signUpProgress = findViewById(R.id.signUpProgress);

        firebaseAuth = FirebaseAuth.getInstance();

        signUp.setOnClickListener(v -> {
            //signUpButtonPress = true;
            //signInButtonPress = false;
            String email = emailUp.getText().toString();
            String pass = passUp.getText().toString();

            if(email.isEmpty()){
                emailUp.setError("Please enter email id..");
                emailUp.requestFocus();
            } else if(pass.isEmpty()){
                passUp.setError("Please enter password..");
                passUp.requestFocus();
            } else {
                signUpProgress.setVisibility(View.VISIBLE);
                firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(task -> {
                    if(!task.isSuccessful()){
                        signUpProgress.setVisibility(View.GONE);
                        Toast.makeText(signUp.this,"Sign up Unsuccessful, please try again.",Toast.LENGTH_SHORT).show();
                    } else {
                        signUpProgress.setVisibility(View.GONE);
                        Log.d("Authentication","User signing up..");
                        intent = new Intent(signUp.this, HomeScreen.class);
                        intent.putExtra("name",userName.getText().toString());
                        intent.putExtra("state", "signedUp");
                        startActivity(intent);
                        signUp.this.finish();
                      }
                });
            }
        });
        toSignIn.setOnClickListener(v->setToSignIn());
    }
    void setToSignIn(){
        intent = new Intent(signUp.this,signIn.class);
        startActivity(intent);
    }
}
