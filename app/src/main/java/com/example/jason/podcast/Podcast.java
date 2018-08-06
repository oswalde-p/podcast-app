package com.example.jason.podcast;

/**
 * Created by jason on 24/05/18.
 */

public class Podcast {
    private String title;
    private String thumbnail;
    private String imgUrl60;

    public Podcast(String name, String imgUrl60) {
        this.setTitle(name);
        this.imgUrl60 = imgUrl60;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getThumbnailSmall(){
        return imgUrl60;
    }

}
