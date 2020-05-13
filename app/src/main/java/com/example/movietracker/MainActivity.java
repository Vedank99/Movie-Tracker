package com.example.movietracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    Intent intent;
    public static DatabaseHelper MyDb;

    boolean onCreateBool = true;
    boolean onStopBool = false;

    public static List<MovieBlock>moviesAdd = new ArrayList<>();
    public static List<MovieBlock>watchListAdd = new ArrayList<>();
    public static List<MovieBlock>watchedAdd = new ArrayList<>();

    String BASE_URL = "https://api.themoviedb.org/3/";
    String API_KEY = "e95363a215389eecb7b8fdd6283b098a";
    String LANGUAGE = "en-US";

    List<Integer>watchListID = new ArrayList<>();
    MovieBlock movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onCreateBool = true;
        onStopBool = false;
        MyDb = new DatabaseHelper(this);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TMDbApi api = retrofit.create(TMDbApi.class);

        Cursor result = MyDb.getWatchListData();

        if (result == null) {
            Log.d("Data", "No Data Available");
        } else {
            while (result.moveToNext()) {
                watchListID.add(Integer.parseInt(result.getString(1)));
            }
            Log.d("WatchListID", "Size " + watchListID.size());
            if (watchListID.size() > 0)
                Log.d("WatchListID", "Last Movie " + watchListID.get(watchListID.size() - 1));
        }
        int i = 0;
        while (i < watchListID.size()) {

            int movieID = watchListID.get(i);

            Call<MovieBlock> call = api.getMovie(movieID, API_KEY, LANGUAGE);
            call.enqueue(new Callback<MovieBlock>() {
                @Override
                public void onResponse(Call<MovieBlock> call, Response<MovieBlock> response) {
                    movie = response.body();
                    Log.d("Movie", "Title " + movie.getTitle());
                    movie.setWatchList(true);
                    movie.setWatched(false);
                    WatchList.allWatchList.add(movie);
                }

                @Override
                public void onFailure(Call<MovieBlock> call, Throwable t) {
                    t.printStackTrace();
                }
            });
            i++;
        }

        watchListID.clear();

        result = MyDb.getWatchedData();

        if (result == null) {
            Log.d("Data", "No Data Available");
        } else {
            while (result.moveToNext()) {
                watchListID.add(Integer.parseInt(result.getString(1)));
            }
            if (watchListID.size() > 0)
                Log.d("WatchedListID", "Last Movie " + watchListID.get(watchListID.size() - 1));
        }
        i = 0;
        while (i < watchListID.size()) {

            int movieID = watchListID.get(i);

            Call<MovieBlock> call = api.getMovie(movieID, API_KEY, LANGUAGE);
            call.enqueue(new Callback<MovieBlock>() {
                @Override
                public void onResponse(Call<MovieBlock> call, Response<MovieBlock> response) {
                    movie = response.body();
                    movie.setWatchList(false);
                    movie.setWatched(true);
                    Watched.allWatchedList.add(movie);
                }

                @Override
                public void onFailure(Call<MovieBlock> call, Throwable t) {
                    t.printStackTrace();
                }
            });
            i++;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
       if(onCreateBool){
            new Handler().postDelayed(() -> {
                setContentView(R.layout.homescreen);
                TextView movieText = findViewById(R.id.popMovies);
                TextView watchList = findViewById(R.id.watchList);
                TextView watched = findViewById(R.id.watched);
                TextView search = findViewById(R.id.search);
                TextView about = findViewById(R.id.about);

                movieText.setOnClickListener(v -> popMovieClick());

                watchList.setOnClickListener(v -> watchListMovieClick());

                watched.setOnClickListener(v -> watchedMovieClick());

                search.setOnClickListener(v -> searchMovieClick());

                about.setOnClickListener(v ->aboutClick());

            },5000);
        }
       if(!onStopBool)
        setContentView(R.layout.activity_main);
       else {
           setContentView(R.layout.homescreen);
           TextView movieText = findViewById(R.id.popMovies);
           TextView watchList = findViewById(R.id.watchList);
           TextView watched = findViewById(R.id.watched);
           TextView search = findViewById(R.id.search);
           TextView about = findViewById(R.id.about);

           movieText.setOnClickListener(v -> popMovieClick());

           watchList.setOnClickListener(v -> watchListMovieClick());

           watched.setOnClickListener(v -> watchedMovieClick());

           search.setOnClickListener(v -> searchMovieClick());

           about.setOnClickListener(v ->aboutClick());
       }
    }

    @Override
    protected void onStop() {
        super.onStop();
        onCreateBool = false;
        onStopBool = true;
    }

    void popMovieClick(){
        intent = new Intent(this,Movies.class);
        startActivity(intent);
    }
    void watchListMovieClick(){
        intent = new Intent(this,WatchList.class);
        startActivity(intent);
    }
    void watchedMovieClick(){
        intent = new Intent(this,Watched.class);
        startActivity(intent);
    }
    void searchMovieClick(){
        intent = new Intent(this,SearchMovies.class);
        startActivity(intent);
    }
    void aboutClick(){
        intent = new Intent(this,about.class);
        startActivity(intent);
    }
}

