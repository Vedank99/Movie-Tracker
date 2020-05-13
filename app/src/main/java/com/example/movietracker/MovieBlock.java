package com.example.movietracker;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MovieBlock {
    @SerializedName("popularity")
    @Expose
    private Double mpopularity;
    @SerializedName("vote_count")
    @Expose
    private Integer voteCount;
    @SerializedName("video")
    @Expose
    private Boolean mvideo;
    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    @SerializedName("id")
    @Expose
    private Integer mid;
    @SerializedName("adult")
    @Expose
    private Boolean madult;
    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;
    @SerializedName("original_language")
    @Expose
    private String originalLanguage;
    @SerializedName("original_title")
    @Expose
    private String originalTitle;
    @SerializedName("genre_ids")
    @Expose
    private List<Integer> genreIds = null;
    @SerializedName("title")
    @Expose
    private String mtitle;
    @SerializedName("vote_average")
    @Expose
    private Double voteAverage;
    @SerializedName("overview")
    @Expose
    private String moverview;
    @SerializedName("release_date")
    @Expose
    private String releaseDate;

    private boolean watchList = false;
    private boolean watched = false;
    private String IMAGE_URL = "https://image.tmdb.org/t/p/original/";

    public boolean getWatchList(){
        return watchList;
    }
    public boolean getWatched(){
        return watched;
    }
    public void setWatchList( boolean watchList ){
        this.watchList = watchList;
    }
    public void setWatched( boolean watched ){
        this.watched = watched;
    }

    public Double getPopularity() {
        return mpopularity;
    }

    public void setPopularity(Double popularity) {
        this.mpopularity = popularity;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public Boolean getVideo() {
        return mvideo;
    }

    public void setVideo(Boolean video) {
        this.mvideo = video;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public Integer getId() {
        return mid;
    }

    public void setId(Integer id) {
        this.mid = id;
    }

    public Boolean getAdult() {
        return madult;
    }

    public void setAdult(Boolean adult) {
        this.madult = adult;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public String getTitle() {
        return mtitle;
    }

    public void setTitle(String title) {
        this.mtitle = title;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getOverview() {
        return moverview;
    }

    public void setOverview(String overview) {
        this.moverview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getIMAGE_URL() {
        return IMAGE_URL;
    }

    public void setIMAGE_URL(String IMAGE_URL) {
        this.IMAGE_URL = IMAGE_URL;
    }
    void onButtonClick(){

    }
}

