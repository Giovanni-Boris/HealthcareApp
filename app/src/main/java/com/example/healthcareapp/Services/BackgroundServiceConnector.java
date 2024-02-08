package com.example.healthcareapp.Services;

import android.content.Context;
import android.content.Intent;

public class BackgroundServiceConnector {
    private Context context;
    private FirebaseFetchService backgroundService;

    public BackgroundServiceConnector(Context context) {
        this.context = context;
    }

    public void startBackgroundService() {
        Intent serviceIntent = new Intent(context, FirebaseFetchService.class);
        context.startService(serviceIntent);
    }

    public void stopBackgroundService() {
        Intent serviceIntent = new Intent(context, FirebaseFetchService.class);
        context.stopService(serviceIntent);
    }


}

