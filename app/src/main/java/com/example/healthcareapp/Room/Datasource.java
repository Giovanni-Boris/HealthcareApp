package com.example.healthcareapp.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.healthcareapp.Entity.User;

@Database(entities = {User.class}, version = 1)
public abstract class Datasource extends RoomDatabase {
    private static Datasource datasourceInstance;
    public abstract UserDAO userDAO();

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