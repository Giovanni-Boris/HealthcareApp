package com.example.healthcareapp.Services;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.healthcareapp.Entity.Register;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FirebaseFetchService extends JobIntentService {
    private static final int JOB_ID = 1000;

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, FirebaseFetchService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        // Tu lógica de recuperación de datos desde Firebase
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Registers");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Register> dataList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Map<String, Object> dataMap = (Map<String, Object>) snapshot.getValue();
                    Register register = Register.fromMap(dataMap);
                    dataList.add(register);
                }
                Log.d("FirebaseFetchService","Tamaño" +dataList.size());
                // Ahora dataList contiene objetos Register deserializados de Firebase
                sendBroadcastData(dataList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void sendBroadcastData(ArrayList<Register> dataList) {
        Intent broadcastIntent = new Intent("DATA_FETCHED_ACTION");
        broadcastIntent.putExtra("dataList",  dataList);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }
}
