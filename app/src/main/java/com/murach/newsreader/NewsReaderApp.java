package com.murach.newsreader;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

/** Allows to store data that applies to the entire app in a central location
 * that is always available to all components of the app including all of the app's
 * activities and services. Also allows to execute code when the application starts
 * rather than executing code each time an activity starts */
public class NewsReaderApp extends Application {
    // Application rozdaetsa kogda proishodit obraschenie k lubome komponentu
    // naprimer esli servis zhivet, to application tozhe

    // these milliseconds correspond with the publication date for the current RSS feed for the app
    private long feedMillis = -1;
    
    public void setFeedMillis(long feedMillis) {
        this.feedMillis = feedMillis;
    }
    
    public long getFeedMillis() {
        return feedMillis;
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("News reader", "App started");

        // Moved
//        Intent service = new Intent(this,NewsReaderService.class);
//        startService(service);
    }
}