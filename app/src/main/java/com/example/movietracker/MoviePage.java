package com.example.movietracker;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MoviePage {
    @SerializedName("page")
    @Expose
    private Integer mpage;
    @SerializedName("total_results")
    @Expose
    private Integer totalResults;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;
    @SerializedName("results")
    @Expose
    private List<MovieBlock> mresults = new ArrayList<>();

    public Integer getPage() {
        return mpage;
    }

    public void setPage(Integer page) {
        this.mpage = page;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public List<MovieBlock> getResults() {
        return mresults;
    }

    public void setResults(List<MovieBlock> results) {
        this.mresults = results;
    }
}
