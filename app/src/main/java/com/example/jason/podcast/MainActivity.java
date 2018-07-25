package com.example.jason.podcast;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * based on https://www.androidhive.info/2016/01/android-working-with-recycler-view/
 */
public class MainActivity extends AppCompatActivity {

    private List<Podcast> podcastList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new PodcastAdapter(podcastList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        recyclerView.setAdapter(mAdapter);


    }

    public void search(View v){
        EditText searchBar = (EditText) findViewById(R.id.search_bar);
        String searchTerm = searchBar.getText().toString();
        search(searchTerm);
        hideKeyboard(this);
    }

    private void search(String term){

        String prefix ="https://itunes.apple.com/search?media=podcast&term=";

        String url = prefix+term;
        Log.d("boo", "searching for " + term);
        httpRequest(url, "get");
    }

    private void httpRequest(String url, String method_s){
        int method = 0;
        if (method_s.toUpperCase().equals("GET")){
            method = Request.Method.GET;
        }else if (method_s.toUpperCase().equals("POST")){
            method = Request.Method.POST;
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(method, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        updatePodcastList(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(stringRequest);
    }

    private void updatePodcastList(String response){
        podcastList.clear();

        try {
            JSONObject obj = new JSONObject(response);
            JSONArray responses = obj.getJSONArray("results");
            Log.d("sd", responses.toString());
            int length = obj.getInt("resultCount");
            for (int i = 0; i < responses.length(); i++){
                JSONObject podcast = responses.getJSONObject(i);
                String name = podcast.getString("trackName");
                String imgUrl = podcast.getString("artworkUrl60");
                Podcast item = new Podcast(name, imgUrl);
                podcastList.add(item);
                Log.d("Name", i + ": " +podcast.getString("trackName"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        mAdapter.notifyDataSetChanged();
        Log.d("feedsList", podcastList.toString());
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
