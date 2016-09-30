package com.murach.newsreader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

    // according to manifest declaration for this BootReceiver
    // it's onReceive will fire when booting action has been completed
    // => Android notifies or "broadcasts" system events
    // and BroadcastReceivers are registered to receive them
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("News reader", "Boot completed");
        
        // start service
        Intent service = new Intent(context, NewsReaderService.class);
        context.startService(service);
    }
}