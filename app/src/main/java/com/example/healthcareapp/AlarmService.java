package com.example.healthcareapp;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.healthcareapp.Entity.Alarm;
import com.example.healthcareapp.Entity.User;
import com.example.healthcareapp.Room.AlarmDAO;
import com.example.healthcareapp.Room.Datasource;

import java.time.LocalDate;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AlarmService extends Service {
    public final String TAG = "AlarmService";

    private Handler handler;
    private Runnable numberCheckRunnable;

    private Datasource datasource;

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
                            Log.d(TAG, "Muestra notificacion");
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
}