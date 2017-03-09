package com.peter.myapp.tv4mostpopular;


import android.graphics.drawable.Drawable;

public class Movie {

    private int imageId;
    String title;
    String description;
    String broadcastTime;
    String imageLink;
    Drawable icon;


    public Movie(String titleItem, String timeItem, String link, Drawable imageItem, String itemDescItem){
        this.title = titleItem;
        this.broadcastTime = timeItem;
        this.imageLink = link;
        this.icon = imageItem;
        this.description = itemDescItem;

    }

    public String getImageLink(){
        return imageLink;
    }

    public Drawable getIcon(){
        return icon;
    }

    public int getImageId(){
        return imageId;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getBroadcastTime() {
        return broadcastTime;
    }

    public String toString(){
        return title;
    }



}
