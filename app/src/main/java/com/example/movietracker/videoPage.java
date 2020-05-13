package com.example.movietracker;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class videoPage {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<videoBlock> results = new ArrayList<videoBlock>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<videoBlock> getResults() {
        return results;
    }

    public void setResults(List<videoBlock> results) {
        this.results = results;
    }
}
