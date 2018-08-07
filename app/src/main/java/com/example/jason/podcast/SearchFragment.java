package com.example.jason.podcast;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class SearchFragment extends Fragment {

    public final static String PODCAST_ID_EXTRA = R.string.package_name + ".ID";
    private List<Podcast> podcastList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_search, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Search");

        recyclerView = getView().findViewById(R.id.recycler_view);

        mAdapter = new PodcastAdapter(podcastList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Podcast podcast = podcastList.get(position);
                select(podcast);
                }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        Button search_b = getView().findViewById(R.id.search_button);
        search_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search(v);
            }
        });
    }

    private void select(Podcast podcast) {
        // do something with selected podcast
        Toast.makeText(getContext(), podcast.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
        PodcastDetailsFragment fragment = new PodcastDetailsFragment();
        fragment.setFeedUrl(podcast.getFeedUrl());
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void search(View v){
        EditText searchBar = getView().findViewById(R.id.search_bar);
        String searchTerm = searchBar.getText().toString();
        search(searchTerm);
        hideKeyboard(getActivity());
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

    private void search(String term){

        String prefix ="https://itunes.apple.com/search?media=podcast&term=";

        String url = prefix+term;
        Log.d("debug", "searching for " + term);
        httpRequest(url, "get");
    }

    private void httpRequest(String url, String method_s){
        int method = 0;
        if (method_s.toUpperCase().equals("GET")){
            method = Request.Method.GET;
        }else if (method_s.toUpperCase().equals("POST")){
            method = Request.Method.POST;
        }
        RequestQueue queue = Volley.newRequestQueue(getContext());
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
            Log.d("debug", responses.toString());
            int length = obj.getInt("resultCount");
            for (int i = 0; i < responses.length(); i++){
                Podcast item = makePod(responses.getJSONObject(i));
                podcastList.add(item);
                Log.d("debug", i + ": " +item.getTitle());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        mAdapter.notifyDataSetChanged();
        Log.d("debug", podcastList.toString());
    }

    private Podcast makePod(JSONObject podJson) throws JSONException {
        String name = podJson.getString("trackName");
        String imgUrl = podJson.getString("artworkUrl60");
        String id = podJson.getString("collectionId");
        Podcast podcast = new Podcast(id, name, imgUrl);
        podcast.setFeedUrl(podJson.getString("feedUrl"));
        return podcast;

    }
}
