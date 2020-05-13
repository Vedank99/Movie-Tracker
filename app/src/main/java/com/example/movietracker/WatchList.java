package com.example.movietracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WatchList extends AppCompatActivity {

    public static List<MovieBlock> watchList = new ArrayList<>();
    public static List<MovieBlock> allWatchList = new ArrayList<>();
    public MovieAdapter watchListAdapter;
    public static Context mContext;
    RecyclerView watchListRecycler;

    String BASE_URL = "https://api.themoviedb.org/3/";
    String API_KEY = "e95363a215389eecb7b8fdd6283b098a";
    String LANGUAGE = "en-US";

//    List<Integer> watchListID = new ArrayList<>();

    MovieBlock movie;

    boolean onCreateBool = false;
    boolean onDestroyedBool = false;
    boolean onRestartBool = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_list);

        onCreateBool = true;

        mContext = this;
        movie = new MovieBlock();
        watchListRecycler = findViewById(R.id.recyclerWatchList);
        watchListRecycler.setLayoutManager(new LinearLayoutManager(mContext));

        watchListAdapter = new MovieAdapter(mContext);

        watchListRecycler.setAdapter(watchListAdapter);

        Log.d("WatchListActivity","Activity Created");

    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d("WatchListActivity", "Activity Started");
        if (onRestartBool) {
            if (!MovieClick.watchListInsert) {
                int pos = MovieClick.pos;
                allWatchList.remove(pos);
                watchListAdapter.removeMovie(pos);
                //watchListID.remove(pos);
            }
        } else {

            watchListAdapter.clearData();

            if (MainActivity.watchListAdd.size() > 0) {
                allWatchList.addAll(MainActivity.watchListAdd);
            }
            for (int i = 0; i < allWatchList.size(); i++) {
                watchList.add(allWatchList.get(i));
                if (i % 9 == 0) {
                    watchListAdapter.updateData(watchList);
                    watchList.clear();
                } else if (i == allWatchList.size() - 1) {
                    watchListAdapter.updateData(watchList);
                    watchList.clear();
                }
            }
            if (allWatchList.size() == 0) {
                setContentView(R.layout.nomovies);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("WatchListActivity","Activity Destroyed");
        onRestartBool = false;
        onDestroyedBool = true;
        onCreateBool = false;
        //watchListID.clear();
        watchList.clear();
        //watchListAdapter.clearData();
        //allWatchList.clear();
        MainActivity.watchListAdd.clear();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("WatchListActivity","Activity Restarted");
        onRestartBool = true;
        onDestroyedBool = false;
        onCreateBool = false;
    }

    void onChange(){
    }
}

