package com.example.movietracker;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TMDbApi {

    @GET("movie/popular")
    Call<MoviePage>getPopular(
        @Query("api_key") String apiKey,
        @Query("language") String language,
        @Query("page") int page
    );

    @GET("movie/top_rated")
    Call<MoviePage>getTopRated(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("movie/upcoming")
    Call<MoviePage>getUpcoming(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("movie/{id}")
    Call<MovieBlock>getMovie(
        @Path("id")int id,
        @Query("api_key") String apiKey,
        @Query("language") String language
    );

    @GET("search/movie")
    Call<MoviePage>getSearch(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("query") String query,
            @Query("page") int page
    );

    @GET("movie/{id}/videos")
    Call<videoPage>getVideoPage(
            @Path("id")int id,
            @Query("api_key")String apiKey,
            @Query("language") String language
    );
}
