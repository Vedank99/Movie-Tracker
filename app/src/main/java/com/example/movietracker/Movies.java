package com.example.movietracker;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.AbsListView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Movies extends AppCompatActivity {
    String BASE_URL = "https://api.themoviedb.org/3/";
    String API_KEY = "e95363a215389eecb7b8fdd6283b098a";
    String LANGUAGE = "en-US";

    public static final String POPULAR = "popular";
    public static final String TOP_RATED = "top_rated";
    public static final String UPCOMING = "upcoming";

    String previousContext = "";
    Intent intent;

    int currentPage = 1;
    public static List<MovieBlock>movies = new ArrayList<>();
    public static List<MovieBlock>allMovies = new ArrayList<>();
    RecyclerView recyclerView;
    LinearLayoutManager manager;
    public static MovieAdapter adapter;
    boolean isScrolling = false;
    int scrolledMovies,visibleMovies,totalMovies;
    public static Context mContext;
    boolean onDestroyBool = false;
    boolean onRestartBool = false;
    boolean onCreateBool = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        Log.d("Activity Current Life","Activity Created.");

        onCreateBool = true;

        mContext = this;

        intent = getIntent();
        previousContext = intent.getStringExtra("type");

        if(previousContext.equals("popular"))
            setTitle("Popular Movies");
        else if(previousContext.equals("top_rated"))
            setTitle("Top Rated Movies");
        else
            setTitle("Upcoming Movies");
        recyclerView = findViewById(R.id.recyclerList);
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter = new MovieAdapter(mContext);
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Activity Current Life","Activity Started.");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TMDbApi api = retrofit.create(TMDbApi.class);

        if(onRestartBool){
            if(MovieClick.watchedInsert||MovieClick.watchListInsert) {
                int pos = MovieClick.pos;
                allMovies.remove(pos);
                adapter.removeMovie(pos);
            }
        } else {

            if(allMovies.size()==0)
                loadMovies(api,currentPage);
            else {
                adapter.clearData();
                if (HomeScreen.moviesAdd.size() > 0) {
                    allMovies.addAll(HomeScreen.moviesAdd);
                }
                for (int i = 0; i < allMovies.size(); i++) {

                    movies.add(allMovies.get(i));
                    if (i % 9 == 0) {
                        adapter.updateData(movies);
                        movies.clear();
                    } else if (i == allMovies.size() - 1) {
                        adapter.updateData(movies);
                        movies.clear();
                    }
                }
            }
        }
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                scrolledMovies = manager.findFirstVisibleItemPosition();
                visibleMovies = manager.getChildCount();
                totalMovies = manager.getItemCount();
                if (isScrolling && (totalMovies - scrolledMovies <= 10) && totalMovies <= 140) {
                    isScrolling = false;
                    ++currentPage;
                    loadMovies(api, currentPage);
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        onCreateBool = false;
        Log.d("Activity Current Life","Activity Paused.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onDestroyBool = true;
        onRestartBool = false;
        onCreateBool = false;
        Log.d("Activity Current Life","Activity Destroyed.");
        movies.clear();
        allMovies.clear();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("Activity Current Life","Activity Restarted.");
        onRestartBool = true;
        onDestroyBool = false;
        onCreateBool = false;
    }
    public void loadMovies(TMDbApi api, int page){
        Call<MoviePage> call;
        if(previousContext.equals("popular"))
            call = api.getPopular(API_KEY,LANGUAGE,page);
        else if(previousContext.equals("top_rated"))
            call = api.getTopRated(API_KEY,LANGUAGE,page);
        else
            call = api.getUpcoming(API_KEY,LANGUAGE,page);
        call.enqueue(new Callback<MoviePage>() {
            @Override
            public void onResponse(Call<MoviePage> call, Response<MoviePage> response) {
                movies = response.body().getResults();
                if(onCreateBool){
                    int i = 0;
                    Cursor cursorWatchList;
                    Cursor cursorWatched;
                    while (i<movies.size()) {
                        cursorWatchList = HomeScreen.MyDb.getWatchListMovie(movies.get(i).getId());
                        cursorWatched = HomeScreen.MyDb.getWatchedMovie(movies.get(i).getId());
                        if (cursorWatchList.getCount() > 0 || cursorWatched.getCount() > 0) {
                            movies.remove(i);
                        } else
                            i++;
                    }
                    if(movies.size()==0){
                        currentPage++;
                        loadMovies(api,currentPage);
                    }
                }
                allMovies.addAll(movies);
                Log.d("Movies","Movies Size "+ allMovies.size());
                adapter.updateData(movies);
            }

            @Override
            public void onFailure(Call<MoviePage> call, Throwable t){
                t.printStackTrace();
            }
        });
    }
}
