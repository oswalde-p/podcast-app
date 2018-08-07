package com.example.jason.podcast;

import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class PodcastDetailsFragment extends Fragment {

    String feedUrl;
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
        if(feedUrl != null){
            updateDisplay();
        }
    }

    public void onResume(){
        super.onResume();
    }

    public void setFeedUrl(String feedUrl){
        this.feedUrl = feedUrl;
    }

    private void updateDisplay(){;
        TextView title = getView().findViewById(R.id.details_title);
        TextView description = getView().findViewById(R.id.details_description);

        HandleXML handler = new HandleXML(feedUrl);
        handler.fetchXML();
        while(handler.parsingComplete);
        title.setText(handler.getTitle());
        description.setText(handler.getDescription());
    }


}