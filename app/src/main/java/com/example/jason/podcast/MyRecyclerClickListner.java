package com.example.jason.podcast;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jason on 26/07/18.
 */

public class MyRecyclerClickListner implements RecyclerTouchListener.ClickListener {
    private List<Podcast> podcastList;
    private Context context;

    MyRecyclerClickListner(Context context, List<Podcast> podcastList){
        this.podcastList = podcastList;
        this.context = context;
    }

    @Override
    public void onClick(View view, int position) {
        Podcast podcast = podcastList.get(position);
        Toast.makeText(this.context, podcast.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLongClick(View view, int position) {

    }
}
