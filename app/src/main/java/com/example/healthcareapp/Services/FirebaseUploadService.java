package com.example.healthcareapp.Services;


import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.example.healthcareapp.Entity.Register;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseUploadService extends JobIntentService {
    private static final int JOB_ID = 1000;

    // Método estático para iniciar el servicio
    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, FirebaseUploadService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Register register = (Register) intent.getSerializableExtra("register");

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference reference = db.getReference("Registers");

        reference.child(register.getHora()).setValue(register).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("Firebase","Guardo en firebase");
            }
        });
    }
}
