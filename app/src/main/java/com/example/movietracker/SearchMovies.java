package com.example.movietracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchMovies extends AppCompatActivity {

    String BASE_URL = "https://api.themoviedb.org/3/";
    String API_KEY = "e95363a215389eecb7b8fdd6283b098a";
    String LANGUAGE = "en-US";
    int Pages = 1;
    int currentPage = 1;

    int scrolledMovies;
    int visibleMovies;
    int totalMovies;

    MovieAdapter searchAdapter;
    String movieName = "";

    List<MovieBlock>searchMovies = new ArrayList<>();
    public static List<MovieBlock>allSearchMovies = new ArrayList<>();
    public static Context mContext;
    boolean isScrolling = false;
    LinearLayoutManager searchManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movies);

        mContext = this;

        SearchView searchView = findViewById(R.id.search);
        RecyclerView searchRecyclerView = findViewById(R.id.searchList);
        searchManager = new LinearLayoutManager(this);
        searchRecyclerView.setLayoutManager(searchManager);
        searchAdapter = new MovieAdapter(this);
        searchRecyclerView.setAdapter(searchAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TMDbApi api = retrofit.create(TMDbApi.class);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                movieName = query;
                loadSearchedMovies(api,query,1);
                searchView.clearFocus();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                    searchAdapter.clearData();
                    allSearchMovies.clear();
                return false;
            }
        });
        searchRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                scrolledMovies = searchManager.findFirstVisibleItemPosition();
                visibleMovies = searchManager.getChildCount();
                totalMovies = searchManager.getItemCount();
                if (isScrolling && (totalMovies - scrolledMovies <= 10) && currentPage<=Pages) {
                    isScrolling = false;
                    ++currentPage;
                    loadSearchedMovies(api,movieName,currentPage);
                }
            }
        });
    }

    void loadSearchedMovies(TMDbApi api,String query,int page){
        Call<MoviePage>call;
        call = api.getSearch(API_KEY,LANGUAGE,query,page);

        call.enqueue(new Callback<MoviePage>() {
            @Override
            public void onResponse(Call<MoviePage> call, Response<MoviePage> response) {
                Pages = response.body().getTotalPages();
                if(Pages == 0){
                    Toast.makeText(mContext,"No such movie exists..",Toast.LENGTH_SHORT).show();;
                    // setContentView(R.layout.nomovies);
                    call.cancel();
                }else{

                    Log.d("Current Page","Current page "+response.body().getPage());
                    Log.d("Current Page","currentPage "+page);

                    searchMovies = response.body().getResults();
                    allSearchMovies.addAll(searchMovies);
                    searchAdapter.updateData(searchMovies);
                    searchMovies.clear();
                }
            }

            @Override
            public void onFailure(Call<MoviePage> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
