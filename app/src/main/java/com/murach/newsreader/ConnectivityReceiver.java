package com.murach.newsreader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class ConnectivityReceiver extends BroadcastReceiver {

    // executed whenever there are changes in connectivity (wifi/internet turned on/off)
    // This code will launch the NewsReaderService whenever internet becomes available
    // and will turn it off when internet becomes unavailable
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("News reader", "Connectivity changed");

        // check if network connection is available
        // get Android's connectivity service
        ConnectivityManager connectivityManager = (ConnectivityManager) 
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = 
                connectivityManager.getActiveNetworkInfo();

        Intent service = new Intent(context, NewsReaderService.class);
        
        if (networkInfo != null && networkInfo.isConnected()){
            Log.d("News reader", "Connected");
            context.startService(service);
        }
        else {
            Log.d("News reader", "NOT connected");
            context.stopService(service);
        }
    }
}