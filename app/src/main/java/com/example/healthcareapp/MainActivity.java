package com.example.healthcareapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.healthcareapp.Fragments.CalendarFragment;
import com.example.healthcareapp.Fragments.HomeFragment;
import com.example.healthcareapp.Services.FirebaseFetchService;
import com.example.healthcareapp.Services.FirebaseForegroundService;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    private boolean isForegroundServiceRunning = false;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    private void startFirebaseFetchService() {
        Intent serviceIntent = new Intent(this, FirebaseFetchService.class);
        this.startService(serviceIntent);
    }

    private void stopFirebaseFetchService() {
        Intent serviceIntent = new Intent(this, FirebaseFetchService.class);
        this.stopService(serviceIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopFirebaseFetchService();

    }

    @Override
    protected void onResume() {
        super.onResume();

        startFirebaseFetchService();
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