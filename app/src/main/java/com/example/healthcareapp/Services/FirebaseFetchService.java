package com.example.healthcareapp.Services;




import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.healthcareapp.Entity.Register;
import com.example.healthcareapp.Repository.RegisterRepository;
import com.example.healthcareapp.Room.Datasource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class FirebaseFetchService extends Service {
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private static final String TAG = "FirebaseFetchService";
    private Handler handler = new Handler();
    private DatabaseReference databaseReference;
    private RegisterRepository registerRepository;
    private Datasource datasource;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"Prendiendo  el servicio");
        datasource = Datasource.newInstance(getApplicationContext());
        registerRepository = new RegisterRepository(datasource.registerDAO());
        databaseReference = FirebaseDatabase.getInstance().getReference("Registers");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.post(fetchDataRunnable);
        return START_STICKY;
    }

    private Runnable fetchDataRunnable = new Runnable() {
        @Override
        public void run() {
            fetchFirebaseData();
            handler.postDelayed(this, 10000); // 60000 milliseconds = 1 minute
        }
    };

    private void fetchFirebaseData() {
        databaseReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot dataSnapshot = task.getResult();
                if (dataSnapshot != null) {
                    ArrayList<Register> dataList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Map<String, Object> dataMap = (Map<String, Object>) snapshot.getValue();
                        Register register = Register.fromMap(dataMap);
                        dataList.add(register);
                    }
                    Log.d(TAG,"Nueva data tamaño"+ dataList.size());

                    chargeData(dataList);
                }
            } else {
                Log.e(TAG, "Error fetching data: ", task.getException());
            }
        });
    }
    private void chargeData(ArrayList<Register> dataList ){
        //Log.d(TAG,"OBSERVABLES PENDIENTES "+compositeDisposable.size());
        Disposable disposable = registerRepository.syncWithFirebase(dataList)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((size) -> {
                    sendBroadcastData(size);
                }, throwable -> {
                    Log.d(TAG,"No se pudo cargar");
                });
        compositeDisposable.add(disposable);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private void sendBroadcastData(int missingRegisters) {
        //Log.d(TAG,"Nuevos valores " + missingRegisters);
        if (missingRegisters == 0) return;
        Intent broadcastIntent = new Intent("DATA_FETCHED_ACTION");
        broadcastIntent.putExtra("new_values",missingRegisters);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"Deteniendo el servicio");
        super.onDestroy();
        compositeDisposable.dispose();
        handler.removeCallbacks(fetchDataRunnable);
    }
}
