package com.example.jason.podcast;//from https://www.tutorialspoint.com/android/android_rss_reader.htm

import android.content.Context;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

public class PodcastRSSHandler {
    private String title;
    private String creator;
    private String description;
    private ArrayList<Episode> episodes;

    private String urlString;
    private XmlPullParserFactory xmlFactoryObject;
    public volatile boolean parsingComplete = true;

    private static final String CREATOR_TAG = "itunes:author";
    private static final String TITLE_TAG = "title";
    private static final String DESCRIPTION_TAG = "description";

    private static final String ITEM_TAG = "item";
    private static final String EPISODE_TITLE_TAG = "title";
    private static final String EPISODE_DATE_TAG = "pubDate";
    private static final String EPISODE_ENCLOSURE_TAG = "enclosure";

    public PodcastRSSHandler(String url, Context context){
        this.urlString = url;
        Log.d("debug", "url: " + url);
        // set default values from strings.xml

        title = context.getResources().getString(R.string.default_title);
        description = context.getResources().getString(R.string.default_description);
        creator = context.getResources().getString(R.string.default_creator);
    }

    public String getTitle(){
        return title;
    }

    public String getCreator(){
        return creator;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<Episode> getEpisodes(){ return episodes;}

    public void parseXML(XmlPullParser parser){
        int event;
        String text = null;
        boolean haveShowDetails = false;

        ArrayList<Episode> episodes = new ArrayList<>();
        Episode newEp = null;
        try{
            event = parser.getEventType();
            while(event != XmlPullParser.END_DOCUMENT){
                String name = parser.getName();

                if(event == XmlPullParser.START_TAG) {

                }else if(event == XmlPullParser.TEXT) {
                    text = parser.getText();

                }else if(event == XmlPullParser.END_TAG) {
                    if (!haveShowDetails){
                        if (name.equals(TITLE_TAG)) {
                            title = text;
                        } else if (name.equals(CREATOR_TAG)) {
                            creator = text;
                            haveShowDetails = true;
                        } else if (name.equals(DESCRIPTION_TAG)) {
                            description = stripTags(text);
                        }

                     }else{
                        if (name.equals(EPISODE_TITLE_TAG)){
                            newEp = new Episode();
                            newEp.title = text;
                        }else if(name.equals(EPISODE_DATE_TAG)){
                            newEp.date = new Date(text);
                        }else if(name.equals(EPISODE_ENCLOSURE_TAG)){

                        }else if(name.equals(ITEM_TAG)){
                            episodes.add(newEp);
                            newEp = null;
                        }
                    }
                }
                event = parser.next();

            }
            parsingComplete = false;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * remove html tags from a string
     * @param text
     * @return
     */
    private String stripTags(String text) {
        //TODO: write this
        Log.d("TODO", "write stripTags");
        return text;
    }

    class Episode{
        String title;
        int length;
        Date date;
    }

    public void fetchXML(){
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run(){
                try{
                    URL url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    conn.setReadTimeout(10000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);

                    // Starts the query
                    conn.connect();
                    InputStream stream = conn.getInputStream();

                    xmlFactoryObject = XmlPullParserFactory.newInstance();
                    XmlPullParser myparser = xmlFactoryObject.newPullParser();

                    myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    myparser.setInput(stream, null);

                    parseXML(myparser);
                    stream.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
