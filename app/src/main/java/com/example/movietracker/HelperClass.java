package com.example.movietracker;

import androidx.annotation.Keep;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Keep
public class HelperClass implements Serializable {

    private List<Integer>userWatchList = new ArrayList<>();
    private List<Integer>userWatchedList = new ArrayList<>();

    @Keep
    public HelperClass(){

    }

    public HelperClass(List<Integer>userWatchList,List<Integer>userWatchedList){
        this.userWatchList = userWatchList;
        this.userWatchedList = userWatchedList;
    }

    public List<Integer> getUserWatchList() {
        return userWatchList;
    }

    public void setUserWatchList(List<Integer> userWatchList) {
        this.userWatchList = userWatchList;
    }

    public List<Integer> getUserWatchedList() {
        return userWatchedList;
    }

    public void setUserWatchedList(List<Integer> userWatchedList) {
        this.userWatchedList = userWatchedList;
    }
}
