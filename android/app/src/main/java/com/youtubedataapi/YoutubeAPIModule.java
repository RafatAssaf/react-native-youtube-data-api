package com.youtubedataapi;

//android Toast for prompts
import android.widget.Toast;

//react imports
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;

import java.util.Map;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class YoutubeAPIModule extends ReactContextBaseJavaModule{

    private static final String DURATION_SHORT_KEY = "SHORT";

    //defining a public string that identifies the file containing the developer's API key
    private static final String PROPERTIES_FILENAME = "youtube.properties";

    private static final long NUMBER_OF_VIDEOS_RETURNED = 25;

    //TODO: move the api key to be a parameter to a setup function to create a YouTube instance
    private static String YOUTUBE_API_KEY = "key not provided";

    /**
     * Define a global instance of a Youtube object, which will be used
     * to make YouTube Data API requests.
     */
    private static YouTube youtube;


    public YoutubeAPIModule(ReactApplicationContext reactContext) {
        super(reactContext); //uses reactContext constructor
    }

    @Override
    public String getName() {
        return "YoutubeAPIModule";
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        constants.put(DURATION_SHORT_KEY, Toast.LENGTH_SHORT);
        return constants;
    }

    @ReactMethod
    public void toastAlert(String message, int duration) {
        Toast.makeText(getReactApplicationContext(), YOUTUBE_API_KEY, duration).show();
    }

    @ReactMethod
    public void setupYoutubeClient(String key) {
        YOUTUBE_API_KEY = key;
        youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest request) throws IOException {
                //no-op function since we don't need any initializations here
            }
        }).setApplicationName("youtube-query-search").build();
    }

    @ReactMethod
    public void search(String query, Callback successCallback, Callback failureCallback) {

        try {
            // Define the API request for retrieving search results.
            YouTube.Search.List search = youtube.search().list("id,snippet");

            YouTube.Channel

            //set api key, query, type, max results count and the wanted fields
            search.setKey(YOUTUBE_API_KEY);
            search.setQ(query);
            search.setType("video");
            search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
//          search.setFields(""); // let's get the full results for now

            SearchListResponse response = search.execute();
            List<SearchResult> searchResponse = response.getItems();
            successCallback.invoke(response.toString());

        } catch (GoogleJsonResponseException e) {
            failureCallback.invoke(e.toString());
        } catch (IOException e) {
            failureCallback.invoke(e.toString());
        } catch (Throwable t) {
            t.printStackTrace();
            failureCallback.invoke(t.toString());
        }

    }
}
