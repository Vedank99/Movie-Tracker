package com.example.movietracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Watched extends AppCompatActivity {

    public static List<MovieBlock>watchedList = new ArrayList<>();
    public static List<MovieBlock>allWatchedList = new ArrayList<>();
    public MovieAdapter watchedListAdapter;
    public static Context mContext;
    RecyclerView watchedListRecycler;

    MovieBlock movie;

    boolean onCreateBool = false;
    boolean onDestroyedBool = false;
    boolean onRestartBool = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watched);
        mContext = this;

        onCreateBool = true;

        movie = new MovieBlock();
        watchedListRecycler = findViewById(R.id.recyclerWatchedList);
        watchedListRecycler.setLayoutManager(new LinearLayoutManager(mContext));

        watchedListAdapter = new MovieAdapter(mContext);

        watchedListRecycler.setAdapter(watchedListAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (onRestartBool) {
            if (!MovieClick.watchedInsert) {
                int pos = MovieClick.pos;
                allWatchedList.remove(pos);
                watchedListAdapter.removeMovie(pos);
                //  watchedListID.remove(pos);
            }
        } else {

            watchedListAdapter.clearData();

            if (MainActivity.watchedAdd.size() > 0) {
                allWatchedList.addAll(MainActivity.watchedAdd);
            }
            for (int i = 0; i < allWatchedList.size(); i++) {
                watchedList.add(allWatchedList.get(i));
                if (i % 9 == 0) {
                    watchedListAdapter.updateData(watchedList);
                    watchedList.clear();
                } else if (i == allWatchedList.size() - 1) {
                    watchedListAdapter.updateData(watchedList);
                    watchedList.clear();
                }
            }
            if (allWatchedList.size() == 0) {
                setContentView(R.layout.nomovies);
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        onRestartBool = false;
        onDestroyedBool = true;
        onCreateBool = false;
        //watchedListAdapter.clearData();
        watchedList.clear();
        //watchedListID.clear();
        //allWatchedList.clear();
        MainActivity.watchedAdd.clear();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        onRestartBool = true;
        onDestroyedBool = false;
        onCreateBool = false;
    }
}
