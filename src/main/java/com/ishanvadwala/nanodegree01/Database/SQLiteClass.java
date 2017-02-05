package com.ishanvadwala.nanodegree01.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.ishanvadwala.nanodegree01.Models.FavouriteMovieListModel;

import java.util.ArrayList;

/**
 * Created by ishanvadwala on 8/28/16.
 */
public class SQLiteClass extends SQLiteOpenHelper {
    private final String TABLE_NAME = "Favourites";
    private final String COL1 = "MovieId";
    private final String COL2 = "MovieName";
    private final String COL3 = "ReleaseYear";
    private final String COL4 = "MoviePlot";
    private final String COL5 = "MovieReview";
    private final String COL6 = "MoviePoster";
    private final String COL7 = "MovieBackdrop";
    private final String COL8 = "MovieRating";
    private static String name = "db";
    private static SQLiteDatabase.CursorFactory factory = null;
    private static int version = 1;
    private Context context;

    private static SQLiteClass dbInstance;
    private FavouriteMovieListModel favouriteMovieListModel;
    private ArrayList<FavouriteMovieListModel> favouritesList;


    public static SQLiteClass getDBInstance(Context context){
        if (dbInstance == null)
            dbInstance = new SQLiteClass(context);
        return dbInstance;
    }

    private SQLiteClass(Context context) {
        super(context, name, null, version);
        this.context = context;
    }

    public void storeMovie(String movieId, String movieName, String releaseDate, String moviePlot, String movieReview, byte[] moviePoster, byte[] movieBackdrop, String movieRating) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, movieId);
        contentValues.put(COL2, movieName);
        contentValues.put(COL3, releaseDate);
        contentValues.put(COL4, moviePlot);
        contentValues.put(COL5, movieReview);
        contentValues.put(COL6, moviePoster);
        contentValues.put(COL7, movieBackdrop);
        contentValues.put(COL8, movieRating);

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME, null, contentValues);
        db.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE = "CREATE TABLE " + TABLE_NAME + "(" + COL1 + " TEXT," + COL2 + " TEXT," + COL3 + " TEXT," + COL4 + " TEXT," + COL5 + " Text," + COL6 + " BLOB, " +COL7+" BLOB, "+COL8+" TEXT)";
        db.execSQL(CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Boolean isPresent(String movieId) {
        SQLiteDatabase db3 = this.getWritableDatabase();
        String queery = "SELECT * FROM " + TABLE_NAME + "" + " WHERE " + COL1 + "=" + movieId;
        Cursor cr = db3.rawQuery(queery, null);
        if (cr.moveToFirst()) {
            db3.close();
            return true;
        }
        else{
            db3.close();
            return false;
        }
    }

    public void deleteFavourite(String movieId) {

        SQLiteDatabase db3 = this.getWritableDatabase();
        String queery = "DELETE FROM " + TABLE_NAME + "" + " WHERE " + COL1 + " = " + movieId;
        db3.execSQL(queery);
        db3.close();


    }

    public ArrayList getMovieList() {
        favouritesList = new ArrayList<>();
        SQLiteDatabase db3 = this.getWritableDatabase();
        String queery = "SELECT * FROM " + TABLE_NAME + "";
        Cursor cr = db3.rawQuery(queery, null);
        cr.moveToFirst();
        for (int i = 0; i < cr.getCount(); i++) {
            favouriteMovieListModel = new FavouriteMovieListModel();
            favouriteMovieListModel.setMovieId(cr.getString(0));
            favouriteMovieListModel.setMovieName(cr.getString(1));
            favouriteMovieListModel.setMovieYear(cr.getString(2));
            favouriteMovieListModel.setMoviePlot(cr.getString(3));
            favouriteMovieListModel.setMovieBackdrop(cr.getBlob(6));
            favouriteMovieListModel.setMoviePoster(cr.getBlob(5));
            favouriteMovieListModel.setMovieRating(cr.getString(7));
            favouritesList.add(favouriteMovieListModel);
            cr.moveToNext();
        }
     //   db3.close();
        return favouritesList;
    }

    public FavouriteMovieListModel getMovie(String movieId){
        SQLiteDatabase db3 = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE "+COL1+" ="+movieId;
        Cursor cursor = db3.rawQuery(query, null);
        cursor.moveToFirst();
        favouriteMovieListModel =  new FavouriteMovieListModel();

        favouriteMovieListModel.setMovieId(cursor.getString(0));
        favouriteMovieListModel.setMovieName(cursor.getString(1));
        favouriteMovieListModel.setMovieYear(cursor.getString(2));
        favouriteMovieListModel.setMoviePlot(cursor.getString(3));
        favouriteMovieListModel.setMovieBackdrop(cursor.getBlob(6));
        favouriteMovieListModel.setMoviePoster(cursor.getBlob(5));
        favouriteMovieListModel.setMovieRating(cursor.getString(7));
       // db3.close();
        return favouriteMovieListModel;
    }

}