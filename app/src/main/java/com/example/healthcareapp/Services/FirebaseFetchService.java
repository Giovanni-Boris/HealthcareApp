package com.example.healthcareapp.Services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.healthcareapp.Entity.Register;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class FirebaseFetchService extends JobService {

    private DatabaseReference reference;
    private ValueEventListener valueEventListener;

    @Override
    public boolean onStartJob(JobParameters params) {
        reference = FirebaseDatabase.getInstance().getReference("Registers");

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Register> dataList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Map<String, Object> dataMap = (Map<String, Object>) snapshot.getValue();
                    Register register = Register.fromMap(dataMap);
                    dataList.add(register);
                }
                Log.d("MiJobService", "Tamaño: " + dataList.size());
                sendBroadcastData(dataList);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        reference.addValueEventListener(valueEventListener);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d("MiJobService", "Deteniendo: ");
        if (reference != null && valueEventListener != null) {
            reference.removeEventListener(valueEventListener);
        }
        return false;
    }

    private void sendBroadcastData(ArrayList<Register> dataList) {
        Intent broadcastIntent = new Intent("DATA_FETCHED_ACTION");
        broadcastIntent.putExtra("dataList", dataList);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }
}
