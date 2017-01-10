package com.mohammed_abdullah_bd1996.moviesappv3.models;

import java.io.Serializable;
//start class
public class ItemModel implements Serializable {
    private String
            poster_path, //for image of poster
            backdrop_path, //for image of backdrop
            overview, //for overview of movie
            release_date, //for release date of movie
            id, //for get id  of movie
            vote_average, //for vote average of shower
            original_title; //for title  of movie
//********************************************************************************************
    //getter and setter the poster path
    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }
//**********************************************************************************************

    //getter and setter the Backdrop  path

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }
//*************************************************************************************************
    //getter and setter the Overview
    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }
//**************************************************************************************************
    //getter and setter release data
    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }
//**************************************************************************************************
    // getter and setter id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
//*************************************************************************************************
    //getter and setter vote_average
    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }
//*************************************************************************************************
    //getter and settre of the title
    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }
}//end of the class

