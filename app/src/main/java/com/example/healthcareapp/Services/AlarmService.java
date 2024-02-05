package com.example.healthcareapp.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class AlarmService extends Service {
    public final String TAG = "AlarmService";

    private Handler handler;
    private Runnable numberCheckRunnable;

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        startAlarmTask();
    }

    private void startAlarmTask() {
        numberCheckRunnable = new Runnable() {
            @Override
            public void run() {
                checkAlarm();
                handler.postDelayed(this, 1000);
            }
        };

        handler.post(numberCheckRunnable);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(numberCheckRunnable);
        super.onDestroy();
    }

    private void checkAlarm(){
        Log.d(TAG, "Verificación alarma...");
    }
}