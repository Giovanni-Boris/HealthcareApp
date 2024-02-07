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
import com.example.healthcareapp.Fragments.HomeFragment;
import com.example.healthcareapp.Fragments.NoticeDialogFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "ActivityMain";
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;

    private boolean isStop = false;
    private boolean showDialog = false;

    private final BroadcastReceiver dataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //ArrayList<Register> dataList = (ArrayList<Register>) intent.getSerializableExtra("dataList");
            Log.d(TAG,"Broadcast");

            if (!isStop && !showDialog)
                showDialogFragment();

            /*
            if (isStop)
                startForegroundService(new Intent(getApplicationContext(), ForegroundService.class));
            else {
                stopService(new Intent(getApplicationContext(), ForegroundService.class));
                if (!showDialog) {
                    showDialogFragment();
                }
            }

             */
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

        buildBroadcastReceiver();

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        drawerToggle = new ActionBarDrawerToggle(this,this.drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        replaceFragments(new HomeFragment());
        navigationView.bringToFront();

        navigationView.setNavigationItemSelectedListener(item -> {
            if(item.getItemId()==R.id.home)replaceFragments(new HomeFragment());
            else if(item.getItemId()==R.id.calendar)replaceFragments(new CalendarFragment());
            else replaceFragments(new CalendarFragment());
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
        stopService(new Intent(this, ForegroundService.class));
        stopService(new Intent(this, AlarmService.class));
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();

        isStop = true;
        startForegroundService(new Intent(this, ForegroundService.class));
    }

    @Override
    protected void onStart() {
        super.onStart();

        isStop = false;
        stopService(new Intent(getApplicationContext(), ForegroundService.class));
    }

    private void showDialogFragment(){
        NoticeDialogFragment ndf = new NoticeDialogFragment();
        ndf.setMessage("¿Desea silenciar la alarma?");
        ndf.setListener(new NoticeDialogFragment.NoticeDialogListener() {
            @Override
            public void onDialogPositiveClick() {
                Log.d(TAG, "Aceptar");
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
}