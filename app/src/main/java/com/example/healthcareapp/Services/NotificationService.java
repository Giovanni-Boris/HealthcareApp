package com.example.healthcareapp.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.example.healthcareapp.R;

public class NotificationService extends Service {
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    public static final int NOTIFICATION_ID = 1;
    private static final String TAG = "NotificationService";
    public NotificationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotifChannel();

        Notification notif = new Notification.Builder(this, CHANNEL_ID)
                .setContentTitle("Foreground Service Demo")
                .setContentText("Demo notif ejecutando...")
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();

        startForeground(NOTIFICATION_ID, notif);

        Log.d(TAG, "onStartCommand()");

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
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