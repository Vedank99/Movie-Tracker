package com.example.movietracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieClick extends YouTubeBaseActivity {

    MovieBlock movie;
    Intent intent;
    Context mContext;
    public static boolean watchListInsert = false;
    public static boolean watchedInsert = false;
    public static boolean isSearched = false;

    public static final String ytAPI_KEY = "AIzaSyA7XooLgKwHsrzmsVf9JHSK_w1RPmryet4";
    String BASE_URL = "https://api.themoviedb.org/3/";
    String API_KEY = "e95363a215389eecb7b8fdd6283b098a";
    String LANGUAGE = "en-US";

    String mContextString = "";
    String ytVideoID = "youtube id";
    boolean ytid;

    YouTubePlayerView youTubePlayerView;
    YouTubePlayer.OnInitializedListener onInitializedListener;

    public static int pos;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_click);

        mContext = this;

        movie = new MovieBlock();
        intent = new Intent();

        intent = getIntent();
        pos = intent.getIntExtra("moviePos",0);
        mContextString = intent.getStringExtra("Context");
        if(mContextString.equals("Movies"))
            movie = Movies.allMovies.get(pos);
        else if(mContextString.equals("WatchList"))
            movie = WatchList.allWatchList.get(pos);
        else if(mContextString.equals("Watched"))
            movie = Watched.allWatchedList.get(pos);
        else if(mContextString.equals("searchMoviesWatchList")||mContextString.equals("searchMoviesWatched")||mContextString.equals("searchMovies")) {
            movie = SearchMovies.allSearchMovies.get(pos);
            isSearched = true;
        }

        Log.d("Movie Title","The movie returned is "+movie.getTitle());

        ImageView poster = findViewById(R.id.posterOnClick);
        TextView rating = findViewById(R.id.ratingOnClick);
        TextView title = findViewById(R.id.titleOnClick);
        TextView releaseYear = findViewById(R.id.releaseYearOnClick);
        TextView description = findViewById(R.id.descriptionOnClick);
        youTubePlayerView = findViewById(R.id.youtube_player);
        TextView playButton = findViewById(R.id.playButton);
        Button b1 = findViewById(R.id.button1OnClick);
        Button b2 = findViewById(R.id.button2OnClick);

        String imageURL = movie.getIMAGE_URL();
        if(movie.getPosterPath()==null)
            Glide.with(this).load(R.drawable.noposter).into(poster);
        else {
            imageURL += movie.getPosterPath();
            Glide.with(this).load(imageURL).into(poster);
        }
        title.setText(movie.getTitle());
        rating.setText(movie.getVoteAverage().toString());
        releaseYear.setText(movie.getReleaseDate());
        description.setText(movie.getOverview());

        int movieId = movie.getId();

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(httpLoggingInterceptor);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        TMDbApi api = retrofit.create(TMDbApi.class);

        Call<videoPage> call = api.getVideoPage(movieId,API_KEY,LANGUAGE);

        call.enqueue(new Callback<videoPage>() {
            @Override
            public void onResponse(Call<videoPage> call, Response<videoPage> response) {
                videoPage videoPage = response.body();
                List<videoBlock>videoBlocks = new ArrayList<>();
                videoBlocks = videoPage.getResults();
                if(videoBlocks.size()>0) {
                    ytid = true;
                    ytVideoID = videoBlocks.get(0).getKey();
                    Log.d("Trailer","Successful");
                }
                else {
                    Toast.makeText(mContext,"Trailer not found",Toast.LENGTH_SHORT).show();
                    ytid = false;
                    ytVideoID = "NULL";
                }
            }

            @Override
            public void onFailure(Call<videoPage> call, Throwable t) {
                t.printStackTrace();
            }
        });

        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(ytVideoID);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(MovieClick.this, "Error initializing..",Toast.LENGTH_SHORT).show();
            }
        };

        playButton.setOnClickListener(v -> youTubePlayerView.initialize(ytAPI_KEY, onInitializedListener));

        if(mContextString.equals("Movies")||mContextString.equals("searchMovies")){
            b1.setText("Add to watchlist");
            b2.setText("Mark as watched");
            watchListInsert = false;
            watchedInsert = false;
        }
        else if(mContextString.equals("WatchList")||mContextString.equals("searchMoviesWatchList")){
            b1.setText("Remove from watchlist");
            b2.setText("Mark as watched");
            watchedInsert = false;
            watchListInsert = true;
        }
        else if(mContextString.equals("Watched")||mContextString.equals("searchMoviesWatched")){
            b1.setText("Add to watchlist");
            b2.setText("remove from watched");
            watchedInsert = true;
            watchListInsert = false;
        }
        else{
            b1.setText("B1");
            b2.setText("B2");
        }

        b1.setOnClickListener(v -> {
            if(!watchListInsert&&!watchedInsert){
                b1.setText("Remove from watchlist");
                b2.setText("Mark as watched");
                watchListInsert = MainActivity.MyDb.insertWatchListData(movieId);
                watchedInsert = false;
                Toast.makeText(this,"Added to watchlist",Toast.LENGTH_SHORT).show();
            }
            else if(watchListInsert){
                b1.setText("Add to watchlist");
                b2.setText("Mark as watched");
                Integer isDeleted = MainActivity.MyDb.deleteWatchListData(movieId);
                if(isDeleted>0) {
                    Toast.makeText(this,"Removed from watchlist",Toast.LENGTH_SHORT).show();
                    watchListInsert = false;
                }
                watchedInsert = false;
            }
            else if(watchedInsert){
                b1.setText("Remove from watchlist");
                b2.setText("Mark as watched");
                watchListInsert = MainActivity.MyDb.insertWatchListData(movieId);
                Integer isDeleted = MainActivity.MyDb.deleteWatchedData(movieId);
                if(isDeleted>0){
                    watchedInsert = false;
                    Toast.makeText(this,"Added to watchlist",Toast.LENGTH_SHORT).show();
                }

            }
        });

        b2.setOnClickListener(v -> {
            if(!watchListInsert&&!watchedInsert){
                b1.setText("Add to watchlist");
                b2.setText("Remove from watched");
                watchedInsert = MainActivity.MyDb.insertWatchedData(movie.getId());
                Toast.makeText(this,"Marked as watched",Toast.LENGTH_SHORT).show();
            }
            else if(watchListInsert){
                b1.setText("Add to watchlist");
                b2.setText("Remove from watched");
                watchedInsert = MainActivity.MyDb.insertWatchedData(movieId);
                Integer isDeleted = MainActivity.MyDb.deleteWatchListData(movieId);
                if(isDeleted>0){
                    Toast.makeText(this,"Marked as watched",Toast.LENGTH_SHORT).show();
                    watchListInsert = false;
                }
            }
            else {
                b1.setText("Add to watchlist");
                b2.setText("Mark as watched");
                Integer isDeleted = MainActivity.MyDb.deleteWatchedData(movieId);
                if(isDeleted>0){
                    Toast.makeText(this,"Removed from watched",Toast.LENGTH_SHORT).show();
                    watchedInsert = false;
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        buttonClick(mContextString);

    }

    void buttonClick(String mContextString){
        movie.setWatchList(watchListInsert);
        movie.setWatched(watchedInsert);
        if(!(mContextString.equals("Movies")||mContextString.equals("searchMovies"))&&!watchListInsert&&!watchedInsert&&!isSearched){
            MainActivity.moviesAdd.add(movie);
        }
        else if(!(mContextString.equals("WatchList")||mContextString.equals("searchMoviesWatchList"))&&watchListInsert){
            MainActivity.watchListAdd.add(movie);
        }
        else if(!(mContextString.equals("Watched")||mContextString.equals("searchMoviesWatched"))&&watchedInsert){
            MainActivity.watchedAdd.add(movie);
        }
    }
}
