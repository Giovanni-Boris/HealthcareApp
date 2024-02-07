package com.example.healthcareapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.DialogFragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.healthcareapp.Entity.Alarm;
import com.example.healthcareapp.Fragments.CalendarFragment;
import com.example.healthcareapp.Fragments.NoticeDialogFragment;
import com.example.healthcareapp.Room.Datasource;

import java.time.LocalDate;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AlarmService extends Service {
    public static final String TAG = "AlarmService";
    public static final String CHANNEL_ID = "AlarmServiceChannel";
    public static final int NOTIFICATION_ID = 2;
    public static final String BC_ACTION = "ALARM_CHECK_ACTION";

    private Handler handler;
    private Runnable numberCheckRunnable;

    private Datasource datasource;

    NotificationManager nm;

    @Override
    public void onCreate() {
        super.onCreate();

        datasource = Datasource.newInstance(getApplicationContext());

        handler = new Handler();
        startAlarmTask();
    }

    private void startAlarmTask() {
        numberCheckRunnable = new Runnable() {
            @Override
            public void run() {
                checkAlarm();
                handler.postDelayed(this, 60*1000);
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

        Completable.fromAction(() -> {
                    List<Alarm> alarmas = datasource.alarmDAO().readAllDAta();

                    for(Alarm a:alarmas){
                        if (a.getDay() == LocalDate.now().getDayOfMonth()){
                            sendBroadcastAlarm();
                            return;
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Log.d(TAG, "Verificacion completa...");
                });
    }

    private void sendBroadcastAlarm(){
        Intent broadcastIntent = new Intent(BC_ACTION);
        //broadcastIntent.putExtra("dataList", dataList);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }

    private void showDialog(){
        NoticeDialogFragment ndf = new NoticeDialogFragment();
        ndf.setListener(new NoticeDialogFragment.NoticeDialogListener() {
            @Override
            public void onDialogPositiveClick() {
                Log.d(TAG, "Aceptar");
            }

            @Override
            public void onDialogNegativeClick() {
                Log.d(TAG, "Cancelar");
            }
        });
    }

    private void simpleInsert(int day) {
        Completable.fromAction(() -> {
                    Alarm a = new Alarm();
                    a.setDay(day);
                    datasource.alarmDAO().insertAlarm(a);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Log.d(TAG, "Insercion realizada");
                });
    }

    private void showNotif(){
        createNotificationChannel();
        nm.notify(NOTIFICATION_ID, buildNotification());
        Log.d(TAG, "Muestra notificacion");
    }

    private Notification buildNotification() {
        Intent notificationIntent = new Intent(this, LoginActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                notificationIntent,
                PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Alarma")
                .setContentText("Presiona para abrir la aplicación")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        return builder.build();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            nm = getSystemService(NotificationManager.class);
            if (nm != null) {
                nm.createNotificationChannel(channel);
            }
        }
    }
}