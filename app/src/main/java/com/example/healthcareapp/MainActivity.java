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

import com.example.healthcareapp.Entity.Register;
import com.example.healthcareapp.Fragments.CalendarFragment;
import com.example.healthcareapp.Fragments.HomeFragment;
import com.example.healthcareapp.Fragments.NoticeDialogFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;

    private final BroadcastReceiver dataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //ArrayList<Register> dataList = (ArrayList<Register>) intent.getSerializableExtra("dataList");
            Log.d(TAG,"Broadcast");
            showDialogFrament();
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

    private void showDialogFrament(){
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

    private void buildBroadcastReceiver(){
        LocalBroadcastManager.getInstance(this).registerReceiver(
                dataReceiver,
                new IntentFilter(AlarmService.BC_ACTION)
        );
    }
}