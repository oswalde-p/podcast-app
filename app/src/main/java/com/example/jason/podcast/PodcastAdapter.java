package com.example.jason.podcast;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by jason on 1/07/18.
 */

public class PodcastAdapter extends RecyclerView.Adapter<PodcastAdapter.MyViewHolder>{

    private List<Podcast> podcastList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public WebView image;

        public MyViewHolder(View view){
            super(view);
            title = view.findViewById(R.id.title);
            image = view.findViewById(R.id.image);
        }
    }

    public PodcastAdapter(List<Podcast> podcastList){
        this.podcastList = podcastList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.podcast_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position){
        Podcast podcast = podcastList.get(position);
        holder.title.setText(podcast.getTitle());
        holder.image.loadUrl(podcast.getThumbnailSmall());
    }

    @Override
    public int getItemCount(){
        return podcastList.size();
    }

    public Bitmap getRemoteImage(final URL aURL) {
        try {
            final URLConnection conn = aURL.openConnection();
            conn.connect();
            final BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            final Bitmap bm = BitmapFactory.decodeStream(bis);
            bis.close();
            return bm;
        } catch (IOException e) {}
        return null;
    }

}
