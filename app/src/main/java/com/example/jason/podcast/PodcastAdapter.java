package com.example.jason.podcast;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by jason on 1/07/18.
 */

public class PodcastAdapter extends RecyclerView.Adapter<PodcastAdapter.MyViewHolder>{

    private List<Podcast> podcastList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public MyViewHolder(View view){
            super(view);
            title = (TextView) view.findViewById(R.id.title);
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
    }

    @Override
    public int getItemCount(){
        return podcastList.size();
    }

}
