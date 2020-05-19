package com.example.movietracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeScreen extends AppCompatActivity {

    Intent intent;
    String state="signingIn";
    FirebaseAuth firebaseAuth;

    public static DatabaseHelper MyDb;

    boolean onCreateBool = true;
    boolean onStopBool = false;

    String username = "";

    public static List<MovieBlock> moviesAdd = new ArrayList<>();
    public static List<MovieBlock>watchListAdd = new ArrayList<>();
    public static List<MovieBlock>watchedAdd = new ArrayList<>();

    String BASE_URL = "https://api.themoviedb.org/3/";
    String API_KEY = "e95363a215389eecb7b8fdd6283b098a";
    String LANGUAGE = "en-US";

    List<Integer>watchListID = new ArrayList<>();
    MovieBlock movie;

    FirebaseDatabase rootNode;
    DatabaseReference databaseReference;
    FirebaseUser currentFirebaseUser ;

    String userID;
    List<Integer> watchList = new ArrayList<>();
    List<Integer> watchedList = new ArrayList<>();
    ProgressBar homescreenProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.homescreen);

        Log.d("Homescreen Activity","Created..");

        onCreateBool = true;
        onStopBool = false;

        intent = getIntent();
        state = intent.getStringExtra("state");

        Log.d("Intent","State "+state);

        rootNode = FirebaseDatabase.getInstance();

        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = currentFirebaseUser.getUid();

        MyDb = new DatabaseHelper(this);
        Cursor cursor = MyDb.getWatchedData();

        Log.d("Cursor","Count on create "+cursor.getCount());

        TextView movieText = findViewById(R.id.Movies);
        TextView watchList = findViewById(R.id.watchList);
        TextView watched = findViewById(R.id.watched);
        TextView logout = findViewById(R.id.logout);
        TextView about = findViewById(R.id.about);
        homescreenProgress = findViewById(R.id.homescreenProgress);

        movieText.setOnClickListener(v -> MovieClick());

        watchList.setOnClickListener(v -> watchListMovieClick());

        watched.setOnClickListener(v -> watchedMovieClick());

        logout.setOnClickListener(v -> logOut());

        about.setOnClickListener(v ->aboutClick());


    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d("Homescreen Activity","Started..");

        if(onCreateBool){

            Cursor cursor;
            databaseReference = rootNode.getReference("users").child(userID);

            if (state.equals("signedUp")){

                MyDb.resetDatabase();
                MyDb.createDB();
                cursor = MyDb.getWatchedData();

                Log.d("Cursor","Count on signUp "+cursor.getCount());
                Log.d("Signed Up","Started..");

                username = intent.getStringExtra("name");

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(username).build();

                currentFirebaseUser.updateProfile(profileUpdates).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("UserName", "User profile updated to "+username);
                    }
                });
            } else if(state.equals("signingIn")){

                MyDb.resetDatabase();
                MyDb.createDB();
                cursor = MyDb.getWatchedData();

                Log.d("Cursor","Count on signIn "+cursor.getCount());

                Log.d("Signed In","Started..");
                homescreenProgress.setVisibility(View.VISIBLE);
                databaseReference.addListenerForSingleValueEvent(valueEventListener);
            } else {
                getMovies();
            }

            if(!state.equals("signedUp"))
            username = currentFirebaseUser.getDisplayName();
            Log.d("Username","Username "+username);
            setTitle("Hello "+username);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        onStopBool = true;
        onCreateBool = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Cursor cursor = MyDb.getWatchedData();

        Log.d("Cursor","Count on logOut "+cursor.getCount());

        WatchList.allWatchList.clear();
        Watched.allWatchedList.clear();

        Log.d("AllWatchedList","Size "+Watched.allWatchedList.size());
        Log.d("Homescreen Activity","Destroyed..");

    }
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            if (dataSnapshot.exists()) {
                Log.d("onDataChange", "Method being called.");

                watchList.clear();
                watchedList.clear();
                Log.d("onDataChange", " Datasnapshot key "+dataSnapshot.getKey());

                HelperClass user = dataSnapshot.getValue(HelperClass.class);

                Log.d("onDataChange", "WatchList size "+user.getUserWatchList().size());

                watchedList.addAll(user.getUserWatchedList());
                watchList.addAll(user.getUserWatchList());

                boolean inserted = false;
                for (int i = 0; i < watchList.size(); i++)
                    inserted = MyDb.insertWatchListData(watchList.get(i));

                for (int i = 0; i < watchedList.size(); i++)
                    inserted = MyDb.insertWatchedData(watchedList.get(i));

                if(inserted)
                    Log.d("Database","Movies inserted from firebase to database");

                getMovies();

            }
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.d("Reading From DataBase", "Error");
        }
    };

    void getMovies(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TMDbApi api = retrofit.create(TMDbApi.class);

        Cursor result = MyDb.getWatchListData();
        Log.d("WatchList DB","Size "+result.getCount());

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
        homescreenProgress.setVisibility(View.GONE);
    }
    void MovieClick(){
        intent = new Intent(this,Discover.class);
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
    void logOut(){

        watchList.clear();
        watchedList.clear();
        HelperClass user = new HelperClass(watchList,watchedList);

        watchListAdd.addAll(WatchList.allWatchList);

        for(int i=0;i<watchListAdd.size();i++){
            watchList.add(watchListAdd.get(i).getId());
        }
        user.setUserWatchList(watchList);

        watchedAdd.addAll(Watched.allWatchedList);

        for(int i=0;i<watchedAdd.size();i++){
            watchedList.add(watchedAdd.get(i).getId());
        }
        user.setUserWatchedList(watchedList);

        watchedAdd.clear();
        watchListAdd.clear();

        if(databaseReference.getKey()!=null)
        databaseReference.removeValue();
        databaseReference.setValue(user);

        FirebaseAuth.getInstance().signOut();
        intent = new Intent(this,signUp.class);
        startActivity(intent);
        finish();

    }
    void aboutClick(){
        intent = new Intent(this,about.class);
        startActivity(intent);
    }
}
