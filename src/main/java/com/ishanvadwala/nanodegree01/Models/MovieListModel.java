package com.ishanvadwala.nanodegree01.Models;

/**
 * Created by ishanvadwala on 7/24/16.
 */
public class MovieListModel {
    String URL, id,name;
    public MovieListModel(){}
    public MovieListModel(String URL,String id, String name){
        this.URL=URL;
        this.id=id;
        this.name=name;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
