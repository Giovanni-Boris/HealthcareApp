package com.example.healthcareapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.healthcareapp.Fragments.CalendarFragment;
import com.example.healthcareapp.Fragments.GraphicFragment;
import com.example.healthcareapp.Fragments.HomeFragment;
import com.example.healthcareapp.Fragments.NoticeDialogFragment;
import com.example.healthcareapp.Room.Datasource;
import com.example.healthcareapp.Services.AlarmService;
import com.example.healthcareapp.Services.FirebaseFetchService;
import com.example.healthcareapp.Services.FirebaseForegroundService;
import com.example.healthcareapp.Services.ForegroundAlarmService;
import com.google.android.material.navigation.NavigationView;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "ActivityMain";
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    private boolean isForegroundServiceRunning = false;

    private boolean isStop = false;
    private boolean showDialog = false;
    private long alarmId;

    private Datasource datasource;

    private final BroadcastReceiver dataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG,"Broadcast");
            alarmId = (long) intent.getSerializableExtra("alarmId");

            if (!isStop && !showDialog)
                showDialogFragment();
        }
    };

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        datasource = Datasource.newInstance(getApplicationContext());

        buildBroadcastReceiver();

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        drawerToggle = new ActionBarDrawerToggle(this,this.drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        startFirebaseFetchService();
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        replaceFragments(new HomeFragment());
        navigationView.bringToFront();

        navigationView.setNavigationItemSelectedListener(item -> {
            if(item.getItemId()==R.id.home)replaceFragments(new HomeFragment());
            else if(item.getItemId()==R.id.calendar)replaceFragments(new CalendarFragment());
            else replaceFragments(new GraphicFragment());
            return true;
        });
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }
    private void replaceFragments(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, ForegroundAlarmService.class));
        stopService(new Intent(this, AlarmService.class));
        stopFirebaseFetchService();
    }

    @Override
    protected void onStop() {
        super.onStop();

        isStop = true;
        startForegroundService(new Intent(this, ForegroundAlarmService.class));
    }

    @Override
    protected void onStart() {
        super.onStart();

        isStop = false;
        stopService(new Intent(getApplicationContext(), ForegroundAlarmService.class));
    }

    private void showDialogFragment(){
        NoticeDialogFragment ndf = new NoticeDialogFragment();
        ndf.setMessage("¿Desea silenciar la alarma?");
        ndf.setListener(new NoticeDialogFragment.NoticeDialogListener() {
            @Override
            public void onDialogPositiveClick() {
                Log.d(TAG, "Aceptar");
                simpleDelete();
                showDialog = false;
            }

            @Override
            public void onDialogNegativeClick() {
                Log.d(TAG, "Cancelar");
                showDialog = false;
            }

            @Override
            public void onDismiss() {
                Log.d(TAG, "onDismiss");
                showDialog = false;
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel");
                showDialog = false;
            }
        });

        ndf.show(getSupportFragmentManager(), NoticeDialogFragment.TAG);
        showDialog = true;
    }

    private void buildBroadcastReceiver(){
        LocalBroadcastManager.getInstance(this).registerReceiver(
                dataReceiver,
                new IntentFilter(AlarmService.BC_ACTION)
        );
    }

    private void simpleDelete() {
        Completable.fromAction(() -> {
                    if (alarmId != -1) {
                        datasource.alarmDAO().deleteAlarmById(alarmId);
                        alarmId = -1;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Log.d(TAG, "Eliminación completa...");
                });
    }

    private void startFirebaseFetchService() {
        //System.out.println("Empezando servicio");
        Intent serviceIntent = new Intent(this, FirebaseFetchService.class);
        this.startService(serviceIntent);
    }

    private void stopFirebaseFetchService() {
        Intent serviceIntent = new Intent(this, FirebaseFetchService.class);
        this.stopService(serviceIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Verificar si el servicio en primer plano está en ejecución
        if (isForegroundServiceRunning) {
            // Detener el servicio en primer plano
            stopForegroundService();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopFirebaseFetchService();
        // Verificar si el servicio en primer plano no está en ejecución
        if (!isForegroundServiceRunning) {
            // Iniciar el servicio en primer plano
            startForegroundService();
        }
    }

    private void startForegroundService() {
        // Iniciar el servicio en primer plano
        Intent serviceIntent = new Intent(this, FirebaseForegroundService.class);
        startForegroundService(serviceIntent);
        isForegroundServiceRunning = true;
    }

    private void stopForegroundService() {
        // Detener el servicio en primer plano

        Intent serviceIntent = new Intent(this, FirebaseForegroundService.class);
        stopService(serviceIntent);
        isForegroundServiceRunning = false;
    }
}