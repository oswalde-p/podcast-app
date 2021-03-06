package com.example.jason.podcast;

/**
 * Created by jason on 24/05/18.
 */

public class Podcast {
    private String title;
    private String thumbnail;
    private String imgUrl60;
    private String id;


    private String feedUrl;

    public Podcast(String id, String name, String imgUrl60) {
        this.setTitle(name);
        this.imgUrl60 = imgUrl60;
        this.id = id;
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

    public String getId(){return id;}

    public String getFeedUrl() {
        return feedUrl;
    }

    public void setFeedUrl(String feedUrl) {
        this.feedUrl = feedUrl;
    }

}
