package com.example.movietracker;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    public static Context context;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        Log.d("MainActivity","Created..");

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        new Handler().postDelayed(() -> {

            if(user!=null){
                Intent intent = new Intent(MainActivity.this, HomeScreen.class);
                intent.putExtra("state","signedIn");
                startActivity(intent);
            } else {
                Intent intent = new Intent(MainActivity.this, signUp.class);
                startActivity(intent);
            }
        },3500);
    }
}


