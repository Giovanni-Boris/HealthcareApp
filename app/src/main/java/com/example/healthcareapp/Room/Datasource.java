package com.example.healthcareapp.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.healthcareapp.Entity.Alarm;
import com.example.healthcareapp.Entity.Register;
import com.example.healthcareapp.Entity.User;

@Database(entities = {User.class, Register.class, Alarm.class}, version = 2)
public abstract class Datasource extends RoomDatabase {
    private static Datasource datasourceInstance;
    public abstract UserDAO userDAO();
    public abstract RegisterDAO registerDAO();
    public abstract AlarmDAO alarmDAO();


    public static Datasource newInstance(Context context) {
        if (datasourceInstance == null) {
            datasourceInstance = Room.databaseBuilder(
                    context,
                    Datasource.class,
                    "healthcare").build();

        }
        return datasourceInstance;
    }

}