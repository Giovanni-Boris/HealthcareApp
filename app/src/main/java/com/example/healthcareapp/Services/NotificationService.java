package com.example.healthcareapp.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.healthcareapp.R;

public class NotificationService extends Service {
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    public static final int NOTIFICATION_ID = 1;
    private static final String TAG = "NotificationService";

    private boolean isStarted = false;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotifChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification notif = buildNotification();

        startForeground(NOTIFICATION_ID, notif);

        Log.d(TAG, "onStartCommand()");

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        stopForeground(true);
    }

    private Notification buildNotification(){
        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        } else {
            builder = new NotificationCompat.Builder(this);
        }

        builder.setContentTitle("Foreground Service")
                .setContentText("Servicio en primer plano ejecutándose")
                .setSmallIcon(R.mipmap.ic_launcher);

        return builder.build();
    }

    private void createNotifChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notifChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager notifManager = getSystemService(NotificationManager.class);
            notifManager.createNotificationChannel(notifChannel);
        }
    }
}