package com.example.jason.podcast;

import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class PodcastDetailsFragment extends Fragment {

    String id;
    ActionBar ab;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.fragment_details, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Details");
        if(id != null){
            updateDisplay();
        }
    }

    public void onResume(){
        super.onResume();
    }

    public void setPodcastId(String id){
        this.id = id;
    }

    private void updateDisplay(){
        TextView title = getView().findViewById(R.id.details_title);
        title.setText(this.id);
    }

}
