package com.ishanvadwala.nanodegree01.Models;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by ishanvadwala on 8/28/16.
 */
public class FavouriteMovieListModel implements Serializable{
    private String movieName;
    private String movieYear;
    private String moviePlot;
    private String movieReview;
    private String movieId;

    public String getMovieRating() {
        return movieRating;
    }

    public void setMovieRating(String movieRating) {
        this.movieRating = movieRating;
    }

    private String movieRating;
    private byte[] moviePoster, movieBackdrop;


    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }



    public byte[] getMoviePoster() {
        return moviePoster;
    }

    public void setMoviePoster(byte[] moviePoster) {
        this.moviePoster = moviePoster;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieYear() {
        return movieYear;
    }

    public void setMovieYear(String movieYear) {
        this.movieYear = movieYear;
    }

    public String getMoviePlot() {
        return moviePlot;
    }

    public void setMoviePlot(String moviePlot) {
        this.moviePlot = moviePlot;
    }

    public String getMovieReview() {
        return movieReview;
    }

    public void setMovieReview(String movieReview) {
        this.movieReview = movieReview;
    }

    public byte[] getMovieBackdrop() {
        return movieBackdrop;
    }

    public void setMovieBackdrop(byte[] movieBackdrop) {
        this.movieBackdrop = movieBackdrop;
    }
}
