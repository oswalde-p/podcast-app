package com.example.jason.podcast;

import android.app.Activity;
import android.graphics.Movie;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

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

    private DrawerLayout mDrawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("debug", "finding + setting actionbar");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.d("debug", "done!");
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        Log.d("debug", "finding+setting recyclerView");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new PodcastAdapter(podcastList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                recyclerView,
                new MyRecyclerClickListner(getApplicationContext(), podcastList) {

        }));

        Log.d("debug", "done!");

        // get nav drawer layout and add listener for events
        Log.d("debug", "finding drawer layout");
        mDrawerLayout = findViewById(R.id.drawer_layout);
        Log.d("debug", "done!");

        Log.d("debug", "finding navView and setting listener");
        NavigationView navigationView = findViewById(R.id.nav_view);
        /*navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here
                        Toast.makeText(getApplicationContext(), menuItem.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();

                        return true;
                    }
                });
                */


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
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
            if(obj.getInt("resultCount") == 0){
                Toast.makeText(getApplicationContext(), "No results :(", Toast.LENGTH_SHORT)
                        .show();
            }else{
                for (int i = 0; i < responses.length(); i++) {
                    JSONObject podcast = responses.getJSONObject(i);
                    String name = podcast.getString("trackName");
                    String imgUrl = podcast.getString("artworkUrl60");
                    Podcast item = new Podcast(name, imgUrl);
                    podcastList.add(item);
                    Log.d("Name", i + ": " + podcast.getString("trackName"));

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        mAdapter.notifyDataSetChanged();
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
