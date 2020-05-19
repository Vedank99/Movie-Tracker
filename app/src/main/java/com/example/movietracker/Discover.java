package com.example.movietracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Discover extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);

        TextView popMovies = findViewById(R.id.popMovies);
        TextView topMovies = findViewById(R.id.trendMovies);
        TextView upcomingMovies = findViewById(R.id.upcomingMovies);
        TextView searchMovies = findViewById(R.id.search);

        popMovies.setOnClickListener(v -> popMovieClick());
        topMovies.setOnClickListener(v -> topMovieClick());
        upcomingMovies.setOnClickListener(v -> upcomingMovieClick());
        searchMovies.setOnClickListener(v -> searchMovieClick());
    }

    void popMovieClick(){
        intent = new Intent(this,Movies.class);
        intent.putExtra("type","popular");
        startActivity(intent);
    }

    void topMovieClick(){
        intent = new Intent(this,Movies.class);
        intent.putExtra("type","top_rated");
        startActivity(intent);
    }

    void upcomingMovieClick(){
        intent = new Intent(this,Movies.class);
        intent.putExtra("type","upcoming");
        startActivity(intent);
    }
    void searchMovieClick(){
        intent = new Intent(this,SearchMovies.class);
        startActivity(intent);
    }
}
