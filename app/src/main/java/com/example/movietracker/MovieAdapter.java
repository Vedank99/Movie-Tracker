package com.example.movietracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {


    private List<MovieBlock>movies;
    private Context context;


    MovieAdapter(Context ct){
        this.context = ct;
        this.movies = new ArrayList<>();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.movieblock,parent,false);
        //this.parent = parent;
        return new MovieViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MovieViewHolder holder, int position) {

        int pos = holder.getAdapterPosition();

        holder.title.setText(movies.get(pos).getTitle());
        holder.rating.setText(movies.get(pos).getVoteAverage().toString());
        holder.releaseYear.setText(movies.get(pos).getReleaseDate());

        String imageURL = movies.get(pos).getIMAGE_URL();
        if(movies.get(pos).getPosterPath()==null){
            Glide.with(context).load(R.drawable.noposter).into(holder.poster);
        }
        else {
            imageURL += movies.get(pos).getPosterPath();
            Glide.with(context).load(imageURL).into(holder.poster);
        }
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context,MovieClick.class);

            String mContextString ="";
            if(context == Movies.mContext){
                mContextString = "Movies";
            }
            else if(context == WatchList.mContext){
                mContextString = "WatchList";
            }
            else if(context == Watched.mContext){
                mContextString = "Watched";
            }
            else if(context == SearchMovies.mContext){
                int movieID = SearchMovies.allSearchMovies.get(holder.getAdapterPosition()).getId();
                Cursor cursorWatchList = HomeScreen.MyDb.getWatchListMovie(movieID);
                Cursor cursorWatched = HomeScreen.MyDb.getWatchedMovie(movieID);
                if(cursorWatchList.getCount()>0){
                    mContextString = "searchMoviesWatchList";
                } else if(cursorWatched.getCount()>0){
                    mContextString = "searchMoviesWatched";
                } else{
                    mContextString = "searchMovies";
                }
            }
            else {
                mContextString = "";
            }
            intent.putExtra("moviePos",holder.getAdapterPosition());
            intent.putExtra("Context",mContextString);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {

        ImageView poster;
        TextView title;
        TextView rating;
        TextView releaseYear;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);

            poster = itemView.findViewById(R.id.poster);
            title = itemView.findViewById(R.id.title);
            rating = itemView.findViewById(R.id.rating);
            releaseYear = itemView.findViewById(R.id.releaseYear);

        }

    }

    public void updateData(List<MovieBlock>ms){
        movies.addAll(ms);
        notifyDataSetChanged();
    }

    public void clearData(){
        int size = movies.size();
        movies.clear();
        notifyItemRangeRemoved(0,size);
    }
    public void removeMovie(int position) {
        movies.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, movies.size());
    }
}
