package com.example.movietracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MovieDatabase.db";
    public static final String WATCHLIST_TABLE = "watchListTable";
    public static final String WATCHED_TABLE = "watchedTable";
    public static final String COL_1 = "Number";
    public static final String COL_2 = "ID";


    public DatabaseHelper(@Nullable Context context) {
        super(context,DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + WATCHLIST_TABLE +" (Number INTEGER PRIMARY KEY AUTOINCREMENT ," + COL_2 +" INTEGER )");
        db.execSQL("create table " + WATCHED_TABLE +" (Number INTEGER PRIMARY KEY AUTOINCREMENT ," + COL_2 +" INTEGER )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+WATCHLIST_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+WATCHED_TABLE);
        onCreate(db);
    }

    public boolean insertWatchListData(int movieID){
        SQLiteDatabase db = this.getWritableDatabase();

        Log.d("MovieID","Movie ID "+movieID);

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,movieID);
        long result = db.insert(WATCHLIST_TABLE,null,contentValues);

        Log.d("Result","Result Value  "+result);

        return result != -1;
    }

    public Integer deleteWatchListData(int movieID){
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(WATCHLIST_TABLE,"ID = ?",new String[] {Integer.toString(movieID)});
    }

    public Cursor getWatchListData(){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("select * from "+WATCHLIST_TABLE,null);
        return res;
    }

    public Cursor getWatchListMovie(int movieID){
        SQLiteDatabase db = this.getWritableDatabase();

        String search = "SELECT * FROM "+WATCHLIST_TABLE+" WHERE "+COL_2+" LIKE '%"+movieID+"%'";
        return db.rawQuery(search ,null);
    }
    public boolean insertWatchedData(int movieID){
        SQLiteDatabase db = this.getWritableDatabase();

        Log.d("MovieID","Movie ID "+movieID);

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,movieID);
        long result = db.insert(WATCHED_TABLE,null,contentValues);

        Log.d("Result","Result Value  "+result);

        return result != -1;
    }

    public Integer deleteWatchedData(int movieID){
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(WATCHED_TABLE,"ID = ?",new String[] {Integer.toString(movieID)});
    }

    public Cursor getWatchedData(){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("select * from "+WATCHED_TABLE,null);
        return res;
    }

    public Cursor getWatchedMovie(int movieID){
        SQLiteDatabase db = this.getWritableDatabase();

        String search = "SELECT * FROM "+WATCHED_TABLE+" WHERE "+COL_2+" LIKE '%"+movieID+"%'";
        return db.rawQuery(search ,null);
    }
}
